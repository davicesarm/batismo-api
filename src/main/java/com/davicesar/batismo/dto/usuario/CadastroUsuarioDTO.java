package com.davicesar.batismo.dto.usuario;

import com.davicesar.batismo.model.Cargo;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CadastroUsuarioDTO(
        @NotBlank
        String email,

        @NotNull
        @Enumerated(EnumType.STRING)
        Cargo cargo,

        @NotNull
        String senha,

        @Size(min = 3)
        String nome,

        @Size(min = 3)
        String marido,

        @Size(min = 3)
        String mulher
) {}
