package com.davicesar.batismo.dto.batizado;

import com.davicesar.batismo.dto.batizando.BatizandoDTO;
import com.davicesar.batismo.dto.usuario.CasalDTO;
import com.davicesar.batismo.model.Batizado;

import java.time.LocalDateTime;
import java.util.List;

public record BatizadoDTO(
        Long id,
        LocalDateTime data,
        String celebrante,
        CasalDTO casal,
        List<BatizandoDTO> batizandos
) {
    public BatizadoDTO(Batizado b) {
        this(
                b.getId(),
                b.getData(),
                b.getCelebrante(),
                new CasalDTO(b.getCasal()),
                b.getCatecumenos().stream().map(BatizandoDTO::new).toList()
        );
    }
}
