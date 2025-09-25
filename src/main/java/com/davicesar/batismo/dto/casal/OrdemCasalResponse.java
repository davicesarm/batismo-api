package com.davicesar.batismo.dto.casal;

import com.davicesar.batismo.model.OrdemCasal;

public record OrdemCasalResponse(
        Long idCasal,
        String marido,
        String mulher,
        Long ordem
) {
    public OrdemCasalResponse(OrdemCasal o) {
        this(
                o.getCasal().getId(),
                o.getCasal().getMarido(),
                o.getCasal().getMulher(),
                o.getOrdem()
        );
    }
}

