package com.davicesar.batismo.repository;

import com.davicesar.batismo.model.Catecumeno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatecumenoRepository extends JpaRepository<Catecumeno, Long> {
}
