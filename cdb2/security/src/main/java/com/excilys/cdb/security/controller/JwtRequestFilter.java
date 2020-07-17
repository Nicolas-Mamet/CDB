package com.excilys.cdb.security.controller;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.excilys.cdb.security.JwtTokenUtil;
import com.excilys.cdb.security.JwtUserDetailsService;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final String authTokenHeaderName = "Authorization";

    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public JwtRequestFilter(JwtUserDetailsService jwtUserDetailsService,
            JwtTokenUtil jwtTokenUtil) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
            throws ServletException, IOException {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            chain.doFilter(request, response);
        }
        final String requestTokenHeader =
                request.getHeader(authTokenHeaderName);
        logger.trace("header : " + requestTokenHeader);
        System.out.println(jwtTokenUtil);
        String username = jwtTokenUtil.getUsernameFromToken(requestTokenHeader);
//        Optional<String> jwtToken =
//                getTokenFromHeader(request.getHeader("Authorization"));
        // JWT Token is in the form "Bearer token". Remove Bearer word and get
        // only the Token
//        try {
//            username = jwtTokenUtil.getUsernameFromToken(jwtToken);
//        } catch (IllegalArgumentException e) {
//            System.out.println("Unable to get JWT Token");
//        } catch (ExpiredJwtException e) {
//            System.out.println("JWT Token has expired");
//        }
        // Once we get the token validate it.
        if (username != null && SecurityContextHolder.getContext()
                .getAuthentication() == null) {

            UserDetails userDetails =
                    this.jwtUserDetailsService.loadUserByUsername(username);

            // if token is valid configure Spring Security to manually set
            // authentication
            if (jwtTokenUtil.validateToken(requestTokenHeader, userDetails)) {

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null,
                                userDetails.getAuthorities());
                authToken
                        .setDetails(new WebAuthenticationDetailsSource()
                                .buildDetails(request));
                // After setting the Authentication in the context, we specify
                // that the current user is authenticated. So it passes the
                // Spring Security Configurations successfully.
                SecurityContextHolder.getContext()
                        .setAuthentication(authToken);
            }
        }
        chain.doFilter(request, response);
    }
}
