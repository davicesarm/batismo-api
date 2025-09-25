package com.davicesar.batismo.controller;

import com.davicesar.batismo.dto.casal.OrdemCasalResponse;
import com.davicesar.batismo.dto.casal.TrocarOrdemDTO;
import com.davicesar.batismo.service.CasalService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CasalController {
    private final CasalService casalService;

    public CasalController(CasalService casalService) {
        this.casalService = casalService;
    }

    @GetMapping("/casais")
    public ResponseEntity<List<OrdemCasalResponse>> listarCasais() {
        return ResponseEntity.ok(casalService.listarCasais());
    }

    @PreAuthorize("hasAuthority('SCOPE_admin') or hasAuthority('SCOPE_coordenador')")
    @PostMapping("/casais/ordem")
    public ResponseEntity<Void> trocarOrdem(@RequestBody TrocarOrdemDTO trocarOrdemDTO) {
        casalService.trocarOrdem(trocarOrdemDTO);
        return ResponseEntity.ok().build();
    }
}
