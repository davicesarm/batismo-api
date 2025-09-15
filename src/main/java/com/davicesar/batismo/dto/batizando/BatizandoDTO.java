package com.davicesar.batismo.dto.batizando;

import com.davicesar.batismo.model.Catecumeno;

public record BatizandoDTO(
        String nome
) {
    public BatizandoDTO(Catecumeno b) {
        this(b.getNome());
    }
}
