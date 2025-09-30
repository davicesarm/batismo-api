package com.davicesar.batismo.service;

import com.davicesar.batismo.dto.batizado.BatizadoResponse;
import com.davicesar.batismo.dto.batizado.BatizadoRequest;
import com.davicesar.batismo.dto.batizado.RealocarCasalDTO;
import com.davicesar.batismo.model.*;
import com.davicesar.batismo.repository.BatizadoRepository;
import com.davicesar.batismo.repository.CatecumenoRepository;
import com.davicesar.batismo.repository.OrdemCasalRepository;
import com.davicesar.batismo.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BatizadoService {
    private final BatizadoRepository batizadoRepository;
    private final CatecumenoRepository catecumenoRepository;
    private final OrdemCasalRepository ordemCasalRepository;
    private final UsuarioRepository usuarioRepository;

    public BatizadoService(
            BatizadoRepository batizadoRepository,
            CatecumenoRepository catecumenoRepository,
            OrdemCasalRepository ordemCasalRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.batizadoRepository = batizadoRepository;
        this.catecumenoRepository = catecumenoRepository;
        this.ordemCasalRepository = ordemCasalRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<BatizadoResponse> listarBatizados(Integer mes, Integer ano) {
        List<Batizado> batizados;

        if (mes != null && ano != null) {
            batizados = batizadoRepository.findByMesAndAno(mes, ano);
        } else if (mes != null) {
            batizados = batizadoRepository.findByMes(mes);
        } else if (ano != null) {
            batizados = batizadoRepository.findByAno(ano);
        } else {
            batizados = batizadoRepository.findAllWithCatecumenos();
        }

        return batizados.stream()
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

    @Transactional
    public void editarBatizado(Long id, BatizadoRequest dto) {
        var batizado = batizadoRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY));

        // TODO: editar catecumenos.
        batizado.setCelebrante(dto.celebrante());
        batizado.setData(dto.data());
//        batizado.setCatecumenos(????);
    }

    @Transactional
    public void realocarCasal(RealocarCasalDTO dto) {
        var batizado = batizadoRepository.findById(dto.idBatizado()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY)
        );
        var usuario = usuarioRepository.findById(dto.idCasal());
        if (usuario.isEmpty() || usuario.get().getCargo() != Cargo.casal) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        batizado.setCasal(usuario.get());
    }

    @Transactional
    public void excluirBatizado(Long id) {
        var batizado = batizadoRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY)
        );
        batizadoRepository.delete(batizado);
    }
}