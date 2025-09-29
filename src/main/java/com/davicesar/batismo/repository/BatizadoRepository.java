package com.davicesar.batismo.repository;

import com.davicesar.batismo.model.Batizado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatizadoRepository extends JpaRepository<Batizado, Long> {

    @Query("""
        SELECT b FROM Batizado b
        JOIN FETCH b.catecumenos
        ORDER BY b.data DESC
        """)
    List<Batizado> findAllWithCatecumenos();

    @Query("""
        SELECT b FROM Batizado b
        LEFT JOIN FETCH b.catecumenos
        WHERE MONTH(b.data) = :mes
        ORDER BY b.data DESC
        """)
    List<Batizado> findByMes(@Param("mes") Integer mes);


    @Query("""
        SELECT b FROM Batizado b
        LEFT JOIN FETCH b.catecumenos
        WHERE YEAR(b.data) = :ano
        AND MONTH(b.data) = :mes
        ORDER BY b.data DESC
        """)
    List<Batizado> findByAno(@Param("ano") Integer ano);

    @Query("""
        SELECT b FROM Batizado b
        LEFT JOIN FETCH b.catecumenos
        WHERE YEAR(b.data) = :ano
        AND MONTH(b.data) = :mes
        ORDER BY b.data DESC
        """)
    List<Batizado> findByMesAndAno(@Param("mes") Integer mes, @Param("ano") Integer ano);
}
