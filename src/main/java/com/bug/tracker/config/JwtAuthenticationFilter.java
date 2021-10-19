package com.bug.tracker.config;

import com.bug.tracker.user.service.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  @Autowired
  private JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                  @NotNull HttpServletResponse httpServletResponse,
                                  @NotNull FilterChain filterChain) throws ServletException, IOException {
    final String requestTokenHeader = httpServletRequest.getHeader("Authorization");
    logger.warn("**********" + requestTokenHeader + "**********");

    String username = null;
    String jwt = null;

    if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
      jwt = requestTokenHeader.substring(7);
      try {
        username = jwtUtil.extractUsername(jwt);
      } catch (ExpiredJwtException e) {
        e.printStackTrace();
        logger.error("JWT has expired");
      } catch (Exception e) {
        e.printStackTrace();
        logger.error("Error in JwtAuthenticationFilter");
      }
    } else {
      logger.warn("Header must starts with Bearer.");
    }

    //Validate
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      if (jwtUtil.validateToken(jwt, userDetails)) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
      } else {
        logger.warn("Token Invalid.");
      }
    } else {
      logger.warn("Username is null or SecurityContextHolder.getContext().getAuthentication() is not null in JwtAuthenticationFilter");
    }

    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }
}
