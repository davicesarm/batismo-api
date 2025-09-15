package com.davicesar.batismo.dto.usuario;

import com.davicesar.batismo.model.Usuario;

public record CasalDTO(
        String marido,
        String mulher
)
{
    public CasalDTO(Usuario usuario) {
        this(usuario.getMarido(), usuario.getMulher());
    }
}
