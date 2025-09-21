package com.davicesar.batismo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdemCasal {

    @Id
    @Column(name = "id_casal", nullable = false)
    private Long idCasal;

    @Column(name = "ordem", unique = false, nullable = false, columnDefinition = "serial")
    private Long ordem;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_casal", referencedColumnName = "id")
    private Usuario casal;
}