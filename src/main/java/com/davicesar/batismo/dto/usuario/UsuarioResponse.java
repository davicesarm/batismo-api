package com.davicesar.batismo.dto.usuario;

import com.davicesar.batismo.model.Usuario;

public record UsuarioResponse(
    Long id,
    String email,
    String cargo,
    String nome,
    String marido,
    String mulher,
    Boolean inativo
) {
    public UsuarioResponse(Usuario u) {
        this(u.getId(), u.getEmail(), u.getCargo().name(), u.getNome(), u.getMarido(), u.getMulher(), u.isInativo());
    }

}
