package com.davicesar.batismo.controller;

import com.davicesar.batismo.dto.batizado.BatizadoResponse;
import com.davicesar.batismo.dto.batizado.BatizadoRequest;
import com.davicesar.batismo.dto.batizado.RealocarCasalDTO;
import com.davicesar.batismo.service.BatizadoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/batizados")
@PreAuthorize("!hasAuthority('SCOPE_redefinir-senha')")
public class BatizadoController {
    private final BatizadoService batizadoService;

    public BatizadoController(BatizadoService batizadoService) {
        this.batizadoService = batizadoService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_admin') or hasAuthority('SCOPE_secretaria')")
    public ResponseEntity<Void> cadastrarBatizado(@RequestBody BatizadoRequest batizado) {
        batizadoService.cadastrarBatizado(batizado);
        return ResponseEntity.ok().build();
    }

    // Editar batizado
    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_admin') or hasAuthority('SCOPE_secretaria')")
    public ResponseEntity<Void> editarBatizado(
            @PathVariable Long id,
            @RequestBody BatizadoRequest dto
    ) {
        batizadoService.editarBatizado(id, dto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/realocar")
    @PreAuthorize("hasAuthority('SCOPE_admin') or hasAuthority('SCOPE_coordenador')")
    public ResponseEntity<Void> realocarCasal(@RequestBody RealocarCasalDTO dto) {
        batizadoService.realocarCasal(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refazer-escala")
    @PreAuthorize("hasAuthority('SCOPE_admin') or hasAuthority('SCOPE_coordenador')")
    public ResponseEntity<Void> refazerEscala() {
        batizadoService.refazerEscala();
        return ResponseEntity.ok().build();
    }

    // Excluir batizado
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_admin') or hasAuthority('SCOPE_secretaria')")
    public ResponseEntity<Void> excluirBatizado(@PathVariable Long id) {
        batizadoService.excluirBatizado(id);
        return ResponseEntity.ok().build();
    }

    // Visualizar batizados
    @GetMapping
    public ResponseEntity<List<BatizadoResponse>> listarBatizados(
            @RequestParam(value = "mes", required = false) Integer mes,
            @RequestParam(value = "ano", required = false) Integer ano
    ) {
        return ResponseEntity.ok(batizadoService.listarBatizados(mes, ano));
    }
}
