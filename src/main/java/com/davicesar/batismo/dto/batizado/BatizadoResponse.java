package com.davicesar.batismo.dto.batizado;

import com.davicesar.batismo.dto.catecumeno.CatecumenoDTO;
import com.davicesar.batismo.dto.casal.CasalResponse;
import com.davicesar.batismo.model.Batizado;

import java.time.LocalDateTime;
import java.util.List;

public record BatizadoResponse(
        Long id,
        LocalDateTime data,
        String celebrante,
        CasalResponse casal,
        List<CatecumenoDTO> catecumenos
) {
    public BatizadoResponse(Batizado b) {
        this(
                b.getId(),
                b.getData(),
                b.getCelebrante(),
                new CasalResponse(b.getCasal()),
                b.getCatecumenos().stream().map(CatecumenoDTO::new).toList()
        );
    }
}
