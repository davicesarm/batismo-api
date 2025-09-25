package com.davicesar.batismo.dto.usuario;

import com.davicesar.batismo.model.Usuario;

public record UsuarioResponse(
    String email,
    String cargo,
    String nome,
    String marido,
    String mulher
) {
    public UsuarioResponse(Usuario u) {
        this(u.getEmail(), u.getCargo().name(), u.getNome(), u.getMarido(), u.getMulher());
    }

}
