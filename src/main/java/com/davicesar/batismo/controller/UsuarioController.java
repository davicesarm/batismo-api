package com.davicesar.batismo.controller;

import com.davicesar.batismo.dto.usuario.UsuarioProfileDTO;
import com.davicesar.batismo.dto.usuario.UsuarioRequest;
import com.davicesar.batismo.dto.usuario.UsuarioResponse;
import com.davicesar.batismo.service.BatizadoService;
import com.davicesar.batismo.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@PreAuthorize("hasAuthority('SCOPE_admin')")
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final BatizadoService batizadoService;

    public UsuarioController(UsuarioService usuarioService, BatizadoService batizadoService) {
        this.usuarioService = usuarioService;
        this.batizadoService = batizadoService;

    }

    @PatchMapping("/{id}/editar")
    public ResponseEntity<Void> editarUsuario(
            @PathVariable Long id,
            @RequestBody UsuarioRequest dto
    ) {
        usuarioService.editarUsuario(id, dto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<Void> inativarUsuario(@PathVariable Long id) {
        usuarioService.inativarUsuario(id);
        batizadoService.refazerEscalaExcluirInativos();
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<Void> reativarUsuario(@PathVariable Long id) {
        usuarioService.reativarUsuario(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @PostMapping
    public ResponseEntity<Void> cadastrarUsuario(@RequestBody UsuarioRequest dto) {
        usuarioService.cadastrarUsuario(dto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<UsuarioProfileDTO> dadosUsuario(@AuthenticationPrincipal Jwt jwt) {
        Long user_id = Long.parseLong(jwt.getSubject());
        UsuarioProfileDTO perfilUsuario = usuarioService.getDadosUsuario(user_id);
        return ResponseEntity.ok(perfilUsuario);
    }

}
