package com.bug.tracker.config;

import com.bug.tracker.user.service.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    @NotNull HttpServletResponse httpServletResponse,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = httpServletRequest.getHeader("Authorization");
        System.out.println("**********" + requestTokenHeader + "**********");

        String username = null;
        String jwt = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwt = requestTokenHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (ExpiredJwtException e) {
                e.printStackTrace();
                System.out.println("JWT has expired");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error in JwtAuthenticationFilter");
            }
        } else {
            System.out.println("requestTokenHeader does not starts with Bearer or is null in JwtAuthenticationFilter Class");
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
                System.out.println("Token Invalid");
            }
        } else {
            System.out.println("Username is null or SecurityContextHolder.getContext().getAuthentication() is not null in JwtAuthenticationFilter");
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
