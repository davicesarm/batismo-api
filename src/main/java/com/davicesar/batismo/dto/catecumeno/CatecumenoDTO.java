package com.davicesar.batismo.dto.catecumeno;

import com.davicesar.batismo.model.Catecumeno;

public record CatecumenoDTO(
        Long id,
        String nome
) {
    public CatecumenoDTO(Catecumeno b) {
        this(b.getId(), b.getNome());
    }
}
