package com.davicesar.batismo.service;

import com.davicesar.batismo.dto.BatizandoDTO;
import com.davicesar.batismo.repository.BatizandoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatizandoService {
    private final BatizandoRepository batizandoRepository;

    public BatizandoService(BatizandoRepository batizandoRepository) {
        this.batizandoRepository = batizandoRepository;
    }

    public List<BatizandoDTO> listarBatizandos() {
        return batizandoRepository.findAll()
                .stream()
                .map(BatizandoDTO::new)
                .toList();
    }

}
