package com.davicesar.batismo.service;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class CookieUtil {
    public ResponseCookie generateJwtCookie(String jwt) {
        return ResponseCookie.from("accessToken", jwt)
                .httpOnly(true)
                .secure(true)         // ESSENCIAL: Envia apenas via HTTPS (em produção)
                .path("/")
                .domain("davicesar.com")
                .maxAge(3600 * 24) // 1 dia
                .sameSite("None")
                .build();
    }

    public ResponseCookie deleteJwtCookie() {
        return ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .domain("davicesar.com")
                .maxAge(0)
                .sameSite("None")
                .build();
    }
}