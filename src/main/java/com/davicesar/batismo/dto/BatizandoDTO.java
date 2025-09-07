package com.davicesar.batismo.dto;

import com.davicesar.batismo.model.Batizando;

public record BatizandoDTO(
        String nome
) {
    public BatizandoDTO(Batizando b) {
        this(b.getNome());
    }
}
