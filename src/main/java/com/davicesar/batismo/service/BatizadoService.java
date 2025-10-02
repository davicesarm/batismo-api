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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
        List<Batizado> batizados = batizadoRepository.findByMesAndAno(mes, ano);
        return batizados.stream()
                .map(BatizadoResponse::new)
                .toList();
    }

    @Transactional
    public void cadastrarBatizado(BatizadoRequest batizadoDTO) {
        if (!intervaloDataValido(batizadoDTO.data())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        Batizado batizado = new Batizado();
        batizado.setData(batizadoDTO.data());

        OrdemCasal casal = ordemCasalRepository.findByOrdem(1L).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY)
        );

        // Define um casal para a criação do batizado (pode ser qualquer um)
        batizado.setCasal(casal.getCasal());
        batizado.setCelebrante(batizadoDTO.celebrante());
        batizadoRepository.save(batizado);

        for (String catNome: batizadoDTO.catecumenos()) {
            Catecumeno cat = new Catecumeno();
            cat.setNome(catNome);
            cat.setBatizado(batizado);
            catecumenoRepository.save(cat);
        }

        alocarAutomaticamente(batizado.getData());
    }

    private boolean intervaloDataValido(LocalDateTime data) {
        return batizadoRepository
                .findByDateRange(
                        //"1:29", pois o intervalo é [start, end) e quero verificar explicitamente
                        // se existe um valor dentro desse intervalo, sem incluir o start
                        data.minusHours(1).minusMinutes(29),
                        data.plusHours(1).plusMinutes(30)
                ).isEmpty();
    }

    /**
     * Quando um batizado é cadastrado, rearranja a escala para
     * manter a ordem dos casais.
     * Passa o casal do batizado seguinte para o anterior e assim
     * sucessivamente. No final, o último batizado fica com o casal da vez.
     *
     * @param dataBatizado é a data que define o intervalo que deve ser rearranjado
     */
    @Transactional
    public void alocarAutomaticamente(LocalDateTime dataBatizado) {
        List<OrdemCasal> casais = ordemCasalRepository.findAll(Sort.by("ordem"));

        var batizados = batizadoRepository.findByDateRange(dataBatizado, LocalDateTime.of(3000, 12, 31, 23, 59, 59));

        for (int i = 0; i < batizados.size() - 1; i++) {
            batizados.get(i).setCasal(batizados.get(i + 1).getCasal());
        }

        Usuario casalDaVez = casais.getFirst().getCasal();
        batizados.getLast().setCasal(casalDaVez);

        int novaOrdem = casais.size();
        for (OrdemCasal o: casais) {
            o.setOrdem((long) novaOrdem);
            novaOrdem = (novaOrdem + 1) % casais.size();
        }
    }

    /**
     * Refaz a escala de todos os batizados depois do dia atual.
     */
    @Transactional
    public void refazerEscala() {
        LocalDateTime amanha = LocalDate.now(ZoneId.of("America/Recife")).plusDays(1).atStartOfDay();
        var batizados = batizadoRepository.findByDateRange(amanha, LocalDateTime.of(3000, 12, 31, 23, 59, 59));
        Queue<OrdemCasal> casais = new LinkedList<>(ordemCasalRepository.findAll(Sort.by("ordem")));
        for (var batizado: batizados) {
            OrdemCasal casalDaVez = casais.poll();
            if (casalDaVez == null) continue;
            batizado.setCasal(casalDaVez.getCasal());
            casais.add(casalDaVez);
        }

        long novaOrdem = 1;
        for (OrdemCasal casal: casais) {
            casal.setOrdem(novaOrdem);
            novaOrdem++;
        }
    }

    @Transactional
    public void editarBatizado(Long id, BatizadoRequest dto) {
        var batizado = batizadoRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY));

        editarCatecumenos(batizado, dto.catecumenos());
        batizado.setCelebrante(dto.celebrante());
        batizado.setData(dto.data());
    }

    @Transactional
    public void editarCatecumenos(Batizado batizado, List<String> catecumenos) {
        if (catecumenos == null || catecumenos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "A lista de catecúmenos não pode ser vazia.");
        }

        batizado.getCatecumenos().clear();

        for (String nomeCatecumeno : catecumenos) {
            var novoCatecumeno = new Catecumeno();
            novoCatecumeno.setNome(nomeCatecumeno);
            novoCatecumeno.setBatizado(batizado);
            batizado.getCatecumenos().add(novoCatecumeno);
        }
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