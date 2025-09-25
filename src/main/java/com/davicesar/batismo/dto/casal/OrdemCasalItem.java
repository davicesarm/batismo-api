package com.davicesar.batismo.dto.casal;

import jakarta.validation.constraints.NotNull;

public record OrdemCasalItem(
        @NotNull
        Long idCasal,

        @NotNull
        Long ordem
) { }
