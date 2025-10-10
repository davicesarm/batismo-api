package com.davicesar.batismo.repository;

import com.davicesar.batismo.model.Batizado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BatizadoRepository extends JpaRepository<Batizado, Long> {
    @Query("""
        SELECT DISTINCT b FROM Batizado b
        LEFT JOIN FETCH b.catecumenos
        WHERE YEAR(b.data) = :ano
        AND MONTH(b.data) = :mes
        ORDER BY b.data ASC
        """)
    List<Batizado> findByMesAndAno(@Param("mes") Integer mes, @Param("ano") Integer ano);

    /**
     * O intervalo de busca é fechado na data de início e aberto na data de fim,
     * ou seja, o critério é {@code data >= dataInicio AND data < dataFim}.
     *
     * @param dataInicio A data e hora de início do intervalo (inclusivo). Não pode ser nulo.
     * @param dataFim    A data e hora de fim do intervalo (exclusivo). Não pode ser nulo.
     * @return Uma {@code List<Batizado>} contendo as entidades encontradas, ordenadas pela
     */
    @Query("""
        SELECT DISTINCT b FROM Batizado b
        LEFT JOIN FETCH b.catecumenos
        WHERE b.data >= :dataInicio AND b.data < :dataFim
        ORDER BY b.data ASC
        """)
    List<Batizado> findByDateRange(@Param("dataInicio") LocalDateTime dataInicio, @Param("dataFim") LocalDateTime dataFim);

    @Query("""
        SELECT DISTINCT b FROM Batizado b
        LEFT JOIN FETCH b.catecumenos
        WHERE b.data >= :dataInicio AND b.data < :dataFim
        AND NOT b.casal_alocado_manualmente
        ORDER BY b.data ASC
        """)
    List<Batizado> findByDateRangeSemAlocacaoManual(@Param("dataInicio") LocalDateTime dataInicio, @Param("dataFim") LocalDateTime dataFim);

}
