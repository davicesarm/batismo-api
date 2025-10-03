package com.davicesar.batismo.controller;

import com.davicesar.batismo.dto.login.LoginRequest;
import com.davicesar.batismo.dto.login.LoginResponse;
import com.davicesar.batismo.dto.login.RedefinirSenhaDTO;
import com.davicesar.batismo.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TokenController {
    private final UsuarioService usuarioService;

    public TokenController (UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = usuarioService.autenticarUsuario(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/redefinir-senha")
    public ResponseEntity<LoginResponse> redefinirSenha(
            @RequestBody RedefinirSenhaDTO dto,
            @AuthenticationPrincipal Jwt jwt
    ) {
        Long user_id = Long.parseLong(jwt.getSubject());
        return ResponseEntity.ok(usuarioService.redefinirSenha(dto, user_id));
    }
}
