package com.davicesar.batismo.dto.usuario;

import com.davicesar.batismo.model.Usuario;

public record UsuarioDTO(
    String email,
    String cargo,
    String nome,
    String marido,
    String mulher
) {
    public UsuarioDTO(Usuario u) {
        this(u.getEmail(), u.getCargo().name(), u.getNome(), u.getMarido(), u.getMulher());
    }

}
