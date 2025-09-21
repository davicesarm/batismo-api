package com.davicesar.batismo.repository;

import com.davicesar.batismo.model.OrdemCasal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface OrdemCasalRepository extends JpaRepository<OrdemCasal, Long> {
}
