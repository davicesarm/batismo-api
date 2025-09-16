package com.davicesar.batismo.dto.usuario;

import com.davicesar.batismo.model.Usuario;

public record CasalDTO(
        Long id,
        String marido,
        String mulher
)
{
    public CasalDTO(Usuario usuario) {
        this(usuario.getId(), usuario.getMarido(), usuario.getMulher());
    }
}
