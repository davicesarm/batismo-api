
package com.davicesar.batismo.controller;

import com.davicesar.batismo.dto.BatizadoDTO;
import com.davicesar.batismo.dto.BatizandoDTO;
import com.davicesar.batismo.service.BatizadoService;
import com.davicesar.batismo.service.BatizandoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BatizandoController {
    private final BatizandoService batizandoService;


    public BatizandoController(BatizandoService batizandoService) {
        this.batizandoService = batizandoService;
    }

    // Visualizar batizandos
    @GetMapping("/batizandos")
    private List<BatizandoDTO> listarBatizandos() {
        return batizandoService.listarBatizandos();
    }
}
