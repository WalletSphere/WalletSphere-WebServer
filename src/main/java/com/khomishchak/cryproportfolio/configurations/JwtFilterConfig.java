package com.khomishchak.cryproportfolio.configurations;

import com.khomishchak.cryproportfolio.services.security.JwtService;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import io.netty.util.internal.StringUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtFilterConfig extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    public JwtFilterConfig(UserDetailsService userDetailsService, JwtService jwtService) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String jwtTokenHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (jwtTokenHeader == null || !jwtTokenHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = jwtTokenHeader.substring(7);

        String username = jwtService.extractUsername(token);

        Authentication currentAuthentication = SecurityContextHolder.getContext().getAuthentication();
        if(!StringUtil.isNullOrEmpty(username) || currentAuthentication == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            boolean tokenIsValid = jwtService.isTokenValid(token, userDetails);

            if(tokenIsValid) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        }

        filterChain.doFilter(request, response);

    }
}
