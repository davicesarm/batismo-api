package com.davicesar.batismo.controller;

import com.davicesar.batismo.dto.batizado.BatizadoDTO;
import com.davicesar.batismo.service.BatizadoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BatismoController {
    private final BatizadoService batizadoService;

    public BatismoController(BatizadoService batizadoService) {
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
    private List<BatizadoDTO> listarBatizados() {
        return batizadoService.listarBatizados();
    }
}
