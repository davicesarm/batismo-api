package com.davicesar.batismo.dto.batizado;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

public record CadastroBatizadoDTO(
        @NotNull
        LocalDateTime data,

        @Size(min = 3)
        String celebrante,

        @NotEmpty
        @Valid
        List<@NotBlank String> catecumenos
)
{ }
