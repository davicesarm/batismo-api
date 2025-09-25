package com.davicesar.batismo.dto.casal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OrdemCasalRequest(
        @NotEmpty
        @Valid
        List<OrdemCasalItem> ordemCasais
) { }
