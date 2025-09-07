package com.davicesar.batismo.service;

import com.davicesar.batismo.dto.CadastroUsuarioDTO;
import com.davicesar.batismo.dto.UsuarioDTO;
import com.davicesar.batismo.model.Usuario;
import com.davicesar.batismo.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<UsuarioDTO> listarUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioDTO::new)
                .toList();
    }

    public void cadastrarUsuario(CadastroUsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario(usuarioDTO);
        usuarioRepository.save(usuario);
    }
}
