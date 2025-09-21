package com.davicesar.batismo.service;

import com.davicesar.batismo.dto.batizado.BatizadoDTO;
import com.davicesar.batismo.dto.batizado.CadastroBatizadoDTO;
import com.davicesar.batismo.model.Batizado;
import com.davicesar.batismo.model.Catecumeno;
import com.davicesar.batismo.model.OrdemCasal;
import com.davicesar.batismo.model.Usuario;
import com.davicesar.batismo.repository.BatizadoRepository;
import com.davicesar.batismo.repository.CatecumenoRepository;
import com.davicesar.batismo.repository.OrdemCasalRepository;
import com.davicesar.batismo.repository.UsuarioRepository;
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
            UsuarioRepository usuarioRepository,
            CatecumenoRepository catecumenoRepository,
            OrdemCasalRepository ordemCasalRepository
    ) {
        this.batizadoRepository = batizadoRepository;
        this.catecumenoRepository = catecumenoRepository;
        this.ordemCasalRepository = ordemCasalRepository;
    }

    public List<BatizadoDTO> listarBatizados() {
        return batizadoRepository.findAllWithCatecumenos()
                .stream()
                .map(BatizadoDTO::new)
                .toList();
    }

    @Transactional
    public void cadastrarBatizado(CadastroBatizadoDTO batizadoDTO) {
        Batizado batizado = new Batizado();
        batizado.setData(batizadoDTO.data());

        Sort sort = Sort.by("ordem");
        List<OrdemCasal> usuarios = ordemCasalRepository.findAll(sort).stream().toList();
        System.out.println(usuarios.size());
        System.out.println(usuarios.getFirst());

        Usuario casalDaVez = usuarios.getFirst().getCasal();

        int novaOrdem = usuarios.size();
        for (OrdemCasal o: usuarios) {
            o.setOrdem((long) novaOrdem);
            novaOrdem = (novaOrdem + 1) % usuarios.size();
        }

        ordemCasalRepository.saveAll(usuarios);

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