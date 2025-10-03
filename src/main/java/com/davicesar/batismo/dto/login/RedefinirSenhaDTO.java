package com.davicesar.batismo.dto.login;

import jakarta.validation.constraints.NotBlank;

public record RedefinirSenhaDTO(
        @NotBlank
        String senhaAntiga,

        @NotBlank
        String senhaNova
) { }
