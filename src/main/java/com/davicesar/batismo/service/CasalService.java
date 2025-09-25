package com.davicesar.batismo.service;

import com.davicesar.batismo.dto.casal.OrdemCasalItem;
import com.davicesar.batismo.dto.casal.OrdemCasalRequest;
import com.davicesar.batismo.dto.casal.OrdemCasalResponse;
import com.davicesar.batismo.repository.OrdemCasalRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public boolean ordemValida(List<OrdemCasalItem> ordemCasais) {
        int n = ordemCasais.size();
        long min = Integer.MAX_VALUE;
        long max = Integer.MIN_VALUE;
        Set<Long> unique = new HashSet<>();

        for (var item: ordemCasais) {
            long ordem = item.ordem();
            if (ordem <= 0) return false;
            min = Math.min(min, ordem);
            max = Math.max(max, ordem);
            unique.add(ordem);
        }

        return min == 1 && max == n && unique.size() == n;
    }

    @Transactional
    public void trocarOrdem(OrdemCasalRequest dto) {
        if (!ordemValida(dto.ordemCasais())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        dto.ordemCasais().sort(Comparator.comparing(OrdemCasalItem::idCasal));

        var ordemCasal = ordemCasalRepository.findAll(Sort.by("idCasal"));

        for (int i = 0; i < ordemCasal.size(); i++) {
            var casalOld = ordemCasal.get(i);

            if (i < dto.ordemCasais().size()) {
                var casalNew = dto.ordemCasais().get(i);
                if (!casalOld.getIdCasal().equals(casalNew.idCasal())) {
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
                }
                casalOld.setOrdem(casalNew.ordem());
            } else {
                casalOld.setOrdem((long) i + 1);
            }
        }
    }
}
