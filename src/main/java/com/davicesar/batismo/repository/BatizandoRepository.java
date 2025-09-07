package com.davicesar.batismo.repository;

import com.davicesar.batismo.model.Batizando;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatizandoRepository extends JpaRepository<Batizando, Long> {
}
