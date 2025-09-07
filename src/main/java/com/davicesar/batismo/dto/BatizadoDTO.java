package com.davicesar.batismo.dto;

import java.time.LocalDateTime;
import java.util.List;

public record BatizadoDTO(
        LocalDateTime data,
        String celebrante,
        CasalDTO casal,
        List<BatizandoDTO> batizandos
) { }
