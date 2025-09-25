package com.davicesar.batismo.controller;

import com.davicesar.batismo.dto.batizado.BatizadoResponse;
import com.davicesar.batismo.dto.batizado.BatizadoRequest;
import com.davicesar.batismo.service.BatizadoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BatizadoController {
    private final BatizadoService batizadoService;

    public BatizadoController(BatizadoService batizadoService) {
        this.batizadoService = batizadoService;
    }

    @PostMapping("/batizados")
    @PreAuthorize("hasAuthority('SCOPE_admin') or hasAuthority('SCOPE_secretaria')")
    public ResponseEntity<Void> cadastrarBatizado(@RequestBody BatizadoRequest batizado) {
        batizadoService.cadastrarBatizado(batizado);
        return ResponseEntity.ok().build();
    }

    // Editar batizado
    private void editarBatizado() {}

    // Excluir batizado
    private void excluirBatizado() {}

    // Visualizar batizados
    @GetMapping("/batizados")
    public ResponseEntity<List<BatizadoResponse>> listarBatizados() {
        return ResponseEntity.ok(batizadoService.listarBatizados());
    }
}
