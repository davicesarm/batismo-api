package com.davicesar.batismo.service;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class CookieUtil {
    public ResponseCookie generateJwtCookie(String jwt) {
        return ResponseCookie.from("accessToken", jwt)
                .httpOnly(true)
                .secure(false)         // ESSENCIAL: Envia apenas via HTTPS (em produção)
                .path("/")
                .maxAge(3600 * 24) // 1 dia
                .sameSite("Lax")
                .build();
    }

    public ResponseCookie deleteJwtCookie() {
        return ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
    }
}