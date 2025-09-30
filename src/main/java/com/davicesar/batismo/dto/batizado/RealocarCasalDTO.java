package com.davicesar.batismo.dto.batizado;

import jakarta.validation.constraints.NotNull;

public record RealocarCasalDTO(
        @NotNull
        Long idBatizado,

        @NotNull
        Long idCasal
) { }
