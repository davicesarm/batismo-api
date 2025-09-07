package com.davicesar.batismo.controller;

import com.davicesar.batismo.dto.CadastroUsuarioDTO;
import com.davicesar.batismo.dto.UsuarioDTO;
import com.davicesar.batismo.service.UsuarioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdminController {
    private final UsuarioService usuarioService;

    public AdminController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/usuarios")
    private List<UsuarioDTO> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    // Cadastrar Usuário
    @PostMapping("/cadastrarUsuario")
    private void cadastrarUsuario(@RequestBody CadastroUsuarioDTO usuario) {
        usuarioService.cadastrarUsuario(usuario);
    }

    // Editar Usuário
    private void editarUsuario() {

    }

    // Excluir Usuário
    private void excluirUsuario() {

    }
}
