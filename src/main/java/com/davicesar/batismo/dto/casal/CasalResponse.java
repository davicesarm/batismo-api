package com.davicesar.batismo.dto.casal;

import com.davicesar.batismo.model.Usuario;

public record CasalResponse(
        Long id,
        String marido,
        String mulher
)
{
    public CasalResponse(Usuario usuario) {
        this(usuario.getId(), usuario.getMarido(), usuario.getMulher());
    }
}
