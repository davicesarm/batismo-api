package com.davicesar.batismo.dto.batizado;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record CadastroBatizadoDTO(
        @NotNull
        LocalDateTime data,

        String celebrante,

        @NotEmpty
        List<String> catecumenos
)
{ }
