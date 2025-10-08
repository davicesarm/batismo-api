package com.davicesar.batismo.service;

import com.davicesar.batismo.dto.batizado.BatizadoResponse;
import com.davicesar.batismo.dto.batizado.BatizadoRequest;
import com.davicesar.batismo.dto.batizado.RealocarCasalDTO;
import com.davicesar.batismo.exception.BatizadoNaoEncontradoException;
import com.davicesar.batismo.exception.CasalNaoEncontradoException;
import com.davicesar.batismo.exception.OperacaoProibidaException;
import com.davicesar.batismo.exception.ProcessamentoInvalidoException;
import com.davicesar.batismo.model.*;
import com.davicesar.batismo.repository.BatizadoRepository;
import com.davicesar.batismo.repository.CatecumenoRepository;
import com.davicesar.batismo.repository.OrdemCasalRepository;
import com.davicesar.batismo.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
            throw new ProcessamentoInvalidoException("Intervalo inválido de data.");
        }

        Batizado batizado = new Batizado();
        batizado.setData(batizadoDTO.data());

        OrdemCasal casal = ordemCasalRepository.findByOrdem(1L).orElseThrow(CasalNaoEncontradoException::new);

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

        // Pega apenas os batizado desde a data do batizado em diante.
        var batizados = batizadoRepository.findByDateRange(dataBatizado, LocalDateTime.of(3000, 12, 31, 23, 59, 59));

        // Usando two pointers para evitar mexer nos batizados cuja
        // alocação manual aconteceu.
        int l = 0, r = 1;
        while (r < batizados.size()) {
            if (!batizados.get(r).isCasal_alocado_manualmente()) {
                batizados.get(l).setCasal(batizados.get(r).getCasal());
                l = r;
            }
            r++;
        }

        // Definindo o casal da vez para o batizado que sobrou.
        Usuario casalDaVez = casais.getFirst().getCasal();
        batizados.get(l).setCasal(casalDaVez);

        int novaOrdem = casais.size();
        for (OrdemCasal o: casais) {
            o.setOrdem((long) novaOrdem);
            novaOrdem = (novaOrdem + 1) % casais.size();
        }
    }

    private boolean intervaloDataValido(LocalDateTime data) {
        return batizadoRepository
                .findByDateRange(
                        //"1:29", pois o intervalo é [start, end) (inclui o start e exclui o end)
                        data.minusHours(1).minusMinutes(29),
                        data.plusHours(1).plusMinutes(30)
                ).isEmpty();
    }

    /**
     * Refaz a escala de todos os batizados depois do dia atual.
     */
    @Transactional
    public void refazerEscala() {
        LocalDate hoje = LocalDate.now(ZoneId.of("America/Recife"));
        LocalDateTime amanha = hoje.plusDays(1).atStartOfDay();
        LocalDateTime futuroDistante = amanha.plusYears(2000);

        var batizados = batizadoRepository.findByDateRange(amanha, futuroDistante);
        Queue<OrdemCasal> casais = new LinkedList<>(ordemCasalRepository.findAll(Sort.by("ordem")));

        for (var batizado: batizados) {
            if (batizado.isCasal_alocado_manualmente()) continue;
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
                () -> new BatizadoNaoEncontradoException(id)
        );

        if (batizado.getData().isBefore(LocalDateTime.now(ZoneId.of("America/Recife")))) {
            throw new OperacaoProibidaException("Não é possível editar um batizado passado.");
        }

        editarCatecumenos(batizado, dto.catecumenos());
        batizado.setCelebrante(dto.celebrante());
        batizado.setData(dto.data());
    }

    @Transactional
    public void editarCatecumenos(Batizado batizado, List<String> catecumenos) {
        if (catecumenos == null || catecumenos.isEmpty()) {
            throw new ProcessamentoInvalidoException("A lista de catecúmenos não pode ser vazia.");
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
        long id = dto.idBatizado();
        var batizado = batizadoRepository.findById(id).orElseThrow(
                () -> new BatizadoNaoEncontradoException(id)
        );
        var usuario = usuarioRepository.findById(dto.idCasal()).orElseThrow(
                () -> new CasalNaoEncontradoException(id)
        );
        if (usuario.getCargo() != Cargo.casal) {
            throw new ProcessamentoInvalidoException("O usuário deve ser um casal...");
        }

        batizado.setCasal_alocado_manualmente(true);
        batizado.setCasal(usuario);
    }

    @Transactional
    public void excluirBatizado(Long id) {
        var batizado = batizadoRepository.findById(id).orElseThrow(
                () -> new BatizadoNaoEncontradoException(id)
        );

        if (batizado.getData().isBefore(LocalDateTime.now(ZoneId.of("America/Recife")))) {
            throw new OperacaoProibidaException("Não é possível excluir um batizado passado.");
        }

        batizadoRepository.delete(batizado);
    }
}