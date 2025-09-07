package com.davicesar.batismo.service;

import com.davicesar.batismo.dto.BatizadoDTO;
import com.davicesar.batismo.dto.BatizandoDTO;
import com.davicesar.batismo.dto.CasalDTO;
import com.davicesar.batismo.repository.BatizadoRepository;
import com.davicesar.batismo.repository.BatizandoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatizadoService {
    private final BatizadoRepository batizadoRepository;

    public BatizadoService(BatizadoRepository batizadoRepository) {
        this.batizadoRepository = batizadoRepository;
    }

    public List<BatizadoDTO> listarBatizados() {
        return batizadoRepository.findAllWithBatizandos()
                .stream()
                .map(b -> new BatizadoDTO(
                        b.getData(),
                        b.getCelebrante(),
                        new CasalDTO(b.getCasal()),
                        b.getBatizandos()
                                .stream()
                                .map(BatizandoDTO::new)
                                .toList()
                ))
                .toList();
    }

}