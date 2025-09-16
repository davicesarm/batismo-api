package com.davicesar.batismo.controller;

import com.davicesar.batismo.dto.catecumeno.CatecumenoDTO;
import com.davicesar.batismo.service.CatecumenoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CatecumenoController {
    private final CatecumenoService catecumenoService;


    public CatecumenoController(CatecumenoService catecumenoService) {
        this.catecumenoService = catecumenoService;
    }

    // Visualizar catecumenos
    @GetMapping("/catecumenos")
    public ResponseEntity<List<CatecumenoDTO>> listarCatecumenos() {
        return ResponseEntity.ok(catecumenoService.listarCatecumenos());
    }
}
