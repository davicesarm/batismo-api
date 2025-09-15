package com.davicesar.batismo.controller;

import com.davicesar.batismo.dto.batizado.BatizadoDTO;
import com.davicesar.batismo.service.BatizadoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BatizadoController {
    private final BatizadoService batizadoService;

    public BatizadoController(BatizadoService batizadoService) {
        this.batizadoService = batizadoService;
    }

    // Cadastrar batizado
    @PostMapping("/cadastrarBatizado")
    private void cadastrarBatizado() {}

    // Editar batizado
    private void editarBatizado() {}

    // Excluir batizado
    private void excluirBatizado() {}

    // Visualizar batizados
    @GetMapping("/batizados")
    public ResponseEntity<List<BatizadoDTO>> listarBatizados() {
        return ResponseEntity.ok(batizadoService.listarBatizados());
    }
}
