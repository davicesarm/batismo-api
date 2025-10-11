package com.davicesar.batismo.controller;

import com.davicesar.batismo.dto.login.LoginRequest;
import com.davicesar.batismo.dto.login.LoginResponse;
import com.davicesar.batismo.dto.login.RedefinirSenhaDTO;
import com.davicesar.batismo.service.CookieUtil;
import com.davicesar.batismo.service.UsuarioService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TokenController {
    private final UsuarioService usuarioService;
    private final CookieUtil cookieUtil;

    public TokenController (UsuarioService usuarioService, CookieUtil cookieUtil) {
        this.usuarioService = usuarioService;
        this.cookieUtil = cookieUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest) {
        ResponseCookie cookie = usuarioService.autenticarUsuario(loginRequest);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @PostMapping("/redefinir_senha")
    public ResponseEntity<LoginResponse> redefinirSenha(
            @RequestBody RedefinirSenhaDTO dto,
            @AuthenticationPrincipal Jwt jwt
    ) {
        Long user_id = Long.parseLong(jwt.getSubject());
        ResponseCookie cookie = usuarioService.redefinirSenha(dto, user_id);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie cookie = cookieUtil.deleteJwtCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }
}
