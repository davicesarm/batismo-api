package com.davicesar.batismo.dto.usuario;

import com.davicesar.batismo.model.Cargo;

public record UsuarioProfileDTO(
        String email,
        Cargo cargo
) { }
