package com.davicesar.batismo.service;

import com.davicesar.batismo.dto.batizado.BatizadoDTO;
import com.davicesar.batismo.dto.batizado.CadastroBatizadoDTO;
import com.davicesar.batismo.dto.catecumeno.CatecumenoDTO;
import com.davicesar.batismo.model.Batizado;
import com.davicesar.batismo.model.Catecumeno;
import com.davicesar.batismo.repository.BatizadoRepository;
import com.davicesar.batismo.repository.CatecumenoRepository;
import com.davicesar.batismo.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BatizadoService {
    private final BatizadoRepository batizadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CatecumenoRepository catecumenoRepository;

    public BatizadoService(BatizadoRepository batizadoRepository, UsuarioRepository usuarioRepository, CatecumenoRepository catecumenoRepository) {
        this.batizadoRepository = batizadoRepository;
        this.usuarioRepository = usuarioRepository;
        this.catecumenoRepository = catecumenoRepository;
    }

    public List<BatizadoDTO> listarBatizados() {
        return batizadoRepository.findAllWithCatecumenos()
                .stream()
                .map(BatizadoDTO::new)
                .toList();
    }

    @Transactional
    public void cadastrarBatizado(CadastroBatizadoDTO batizadoDTO) {
        var usuario = usuarioRepository.findById(batizadoDTO.casal_id());
        if (usuario.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Batizado batizado = new Batizado();
        batizado.setData(batizadoDTO.data());
        batizado.setCasal(usuario.get());
        batizado.setCelebrante(batizadoDTO.celebrante());
        batizadoRepository.save(batizado);

        for (String catNome: batizadoDTO.catecumenos()) {
            Catecumeno cat = new Catecumeno();
            cat.setNome(catNome);
            cat.setBatizado(batizado);
            catecumenoRepository.save(cat);
        }

    }
}