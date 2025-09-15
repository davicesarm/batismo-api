package com.davicesar.batismo.service;

import com.davicesar.batismo.dto.batizando.BatizandoDTO;
import com.davicesar.batismo.repository.CatecumenoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatecumenoService {
    private final CatecumenoRepository catecumenoRepository;

    public CatecumenoService(CatecumenoRepository catecumenoRepository) {
        this.catecumenoRepository = catecumenoRepository;
    }

    public List<BatizandoDTO> listarBatizandos() {
        return catecumenoRepository.findAll()
                .stream()
                .map(BatizandoDTO::new)
                .toList();
    }

}
