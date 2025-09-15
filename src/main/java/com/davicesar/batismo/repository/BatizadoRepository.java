package com.davicesar.batismo.repository;

import com.davicesar.batismo.model.Batizado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatizadoRepository extends JpaRepository<Batizado, Long> {

    @Query("SELECT b FROM Batizado b JOIN FETCH b.catecumenos")
    List<Batizado> findAllWithCatecumenos();
}
