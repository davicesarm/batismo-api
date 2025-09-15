package com.davicesar.batismo.controller;

import com.davicesar.batismo.dto.login.LoginRequest;
import com.davicesar.batismo.dto.login.LoginResponse;
import com.davicesar.batismo.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.sound.midi.SysexMessage;

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
}
