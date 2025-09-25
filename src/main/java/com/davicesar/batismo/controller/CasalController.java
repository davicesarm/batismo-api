package com.davicesar.batismo.controller;

import com.davicesar.batismo.dto.casal.OrdemCasalRequest;
import com.davicesar.batismo.dto.casal.OrdemCasalResponse;
import com.davicesar.batismo.service.CasalService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @PatchMapping("/casais/ordem")
    public ResponseEntity<Void> trocarOrdem(@Valid @RequestBody OrdemCasalRequest dto) {
        casalService.trocarOrdem(dto);
        return ResponseEntity.ok().build();
    }
}
