package com.davicesar.batismo.repository;

import com.davicesar.batismo.model.OrdemCasal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface OrdemCasalRepository extends JpaRepository<OrdemCasal, Long> {
    Optional<OrdemCasal> findByOrdem(Long ordem);
}
