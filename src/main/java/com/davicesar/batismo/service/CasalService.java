package com.davicesar.batismo.service;

import com.davicesar.batismo.dto.casal.OrdemCasalResponse;
import com.davicesar.batismo.dto.casal.TrocarOrdemDTO;
import com.davicesar.batismo.repository.OrdemCasalRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CasalService {
    private final OrdemCasalRepository ordemCasalRepository;

    public CasalService(OrdemCasalRepository ordemCasalRepository) {
        this.ordemCasalRepository = ordemCasalRepository;
    }

    public List<OrdemCasalResponse> listarCasais() {
        Sort sort = Sort.by("ordem");
        return ordemCasalRepository
                .findAll(sort)
                .stream()
                .map(OrdemCasalResponse::new)
                .toList();
    }

    @Transactional
    public void trocarOrdem(TrocarOrdemDTO dto) {
        var casal1 = ordemCasalRepository.findById(dto.idCasal1())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNPROCESSABLE_ENTITY, "Casal 1 não encontrado com id " + dto.idCasal1()
                ));;

        var casal2 = ordemCasalRepository.findById(dto.idCasal2())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNPROCESSABLE_ENTITY, "Casal 2 não encontrado com id " + dto.idCasal2()
                ));;
        Long casal1Ordem = casal1.getOrdem();
        casal1.setOrdem(casal2.getOrdem());
        casal2.setOrdem(casal1Ordem);
    }

//    public void setarOrdens(SetarOrdensDTO dto) {
//    }

}
