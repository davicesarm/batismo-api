package com.davicesar.batismo.service;

import com.davicesar.batismo.dto.batizado.BatizadoDTO;
import com.davicesar.batismo.repository.BatizadoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatizadoService {
    private final BatizadoRepository batizadoRepository;

    public BatizadoService(BatizadoRepository batizadoRepository) {
        this.batizadoRepository = batizadoRepository;
    }

    public List<BatizadoDTO> listarBatizados() {
        return batizadoRepository.findAllWithCatecumenos()
                .stream()
                .map(BatizadoDTO::new)
                .toList();
    }

}