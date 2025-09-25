package com.davicesar.batismo.service;

import com.davicesar.batismo.dto.batizado.BatizadoResponse;
import com.davicesar.batismo.dto.batizado.BatizadoRequest;
import com.davicesar.batismo.model.Batizado;
import com.davicesar.batismo.model.Catecumeno;
import com.davicesar.batismo.model.OrdemCasal;
import com.davicesar.batismo.model.Usuario;
import com.davicesar.batismo.repository.BatizadoRepository;
import com.davicesar.batismo.repository.CatecumenoRepository;
import com.davicesar.batismo.repository.OrdemCasalRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatizadoService {
    private final BatizadoRepository batizadoRepository;
    private final CatecumenoRepository catecumenoRepository;
    private final OrdemCasalRepository ordemCasalRepository;

    public BatizadoService(
            BatizadoRepository batizadoRepository,
            CatecumenoRepository catecumenoRepository,
            OrdemCasalRepository ordemCasalRepository
    ) {
        this.batizadoRepository = batizadoRepository;
        this.catecumenoRepository = catecumenoRepository;
        this.ordemCasalRepository = ordemCasalRepository;
    }

    public List<BatizadoResponse> listarBatizados() {
        return batizadoRepository.findAllWithCatecumenos()
                .stream()
                .map(BatizadoResponse::new)
                .toList();
    }

    @Transactional
    public void cadastrarBatizado(BatizadoRequest batizadoDTO) {
        Batizado batizado = new Batizado();
        batizado.setData(batizadoDTO.data());

        Sort sort = Sort.by("ordem");
        List<OrdemCasal> usuarios = ordemCasalRepository.findAll(sort).stream().toList();

        Usuario casalDaVez = usuarios.getFirst().getCasal();

        int novaOrdem = usuarios.size();
        for (OrdemCasal o: usuarios) {
            o.setOrdem((long) novaOrdem);
            novaOrdem = (novaOrdem + 1) % usuarios.size();
        }

        batizado.setCelebrante(batizadoDTO.celebrante());
        batizado.setCasal(casalDaVez);
        batizadoRepository.save(batizado);

        for (String catNome: batizadoDTO.catecumenos()) {
            Catecumeno cat = new Catecumeno();
            cat.setNome(catNome);
            cat.setBatizado(batizado);
            catecumenoRepository.save(cat);
        }

    }
}