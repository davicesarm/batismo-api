package com.davicesar.batismo.controller;

import com.davicesar.batismo.dto.usuario.UsuarioRequest;
import com.davicesar.batismo.dto.usuario.UsuarioResponse;
import com.davicesar.batismo.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@PreAuthorize("hasAuthority('SCOPE_admin')")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @PostMapping("/usuarios")
    public ResponseEntity<Void> cadastrarUsuario(@RequestBody UsuarioRequest dto) {
        usuarioService.cadastrarUsuario(dto);
        return ResponseEntity.ok().build();
    }

}
