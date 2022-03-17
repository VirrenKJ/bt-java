package com.bug.tracker.config;

import com.bug.tracker.config.tenantConfig.TenantContext;
import com.bug.tracker.user.dto.UserTO;
import com.bug.tracker.user.entity.UserBO;
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
import java.util.Objects;

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
    final String userId = httpServletRequest.getHeader("user-id");
    final String role = httpServletRequest.getHeader("role");

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
      // Setting User into UserSessionContext
      UserSessionContext.setCurrentTenant((UserBO) userDetails);

      if (jwtUtil.validateToken(jwt, userDetails) && checkUserRole((UserBO) userDetails, userId, role)) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        //Set current tenant
        TenantContext.setCurrentTenant(httpServletRequest.getHeader("x-tenant"));
      } else {
        logger.warn("Token Invalid.");
      }
    } else {
      logger.warn("Username is null or SecurityContextHolder.getContext().getAuthentication() is not null in JwtAuthenticationFilter");
    }

    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }

  private boolean checkUserRole(UserBO userBO, String userId, String role) {
    Integer id = null;
    if (userId != null) {
      id = Integer.valueOf(userId);
    }
    if (id != null && role != null) {
      if (Objects.equals(userBO.getId(), id) && userBO.getRoles().get(0).getRoleName().equals(role)) {
        return true;
      }
      logger.error("User id or Role Does not match");
      return false;
    }
    logger.error("User id or Role is null");
    return false;
  }
}
