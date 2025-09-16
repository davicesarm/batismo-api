package com.davicesar.batismo.dto.usuario;

import jakarta.validation.constraints.NotNull;

public record CadastroUsuarioDTO(
        @NotNull
        String email,

        @NotNull
        String cargo,

        @NotNull
        String senha,

        String nome,
        String marido,
        String mulher
) {}
