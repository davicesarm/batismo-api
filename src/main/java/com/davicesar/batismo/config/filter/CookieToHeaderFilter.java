package com.davicesar.batismo.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CookieToHeaderFilter extends OncePerRequestFilter {

    private static final String COOKIE_NAME = "accessToken";
    private static final String HEADER_NAME = "Authorization";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(COOKIE_NAME)) {
                    String jwtToken = cookie.getValue();
                    String authHeaderValue = "Bearer " + jwtToken;
                    HttpServletRequest wrapper = new CustomHeaderHttpServletRequest(request, HEADER_NAME, authHeaderValue);
                    filterChain.doFilter(wrapper, response);
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}