package com.davicesar.batismo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class CookieUtil {
    @Value("${DOMAIN:localhost}")
    private String domain;

    public ResponseCookie generateJwtCookie(String jwt) {
        return ResponseCookie.from("accessToken", jwt)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .domain(domain)
                .maxAge(3600 * 24) // 1 dia
                .sameSite("None")
                .build();
    }

    public ResponseCookie deleteJwtCookie() {
        return ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .domain(domain)
                .maxAge(0)
                .sameSite("None")
                .build();
    }
}