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

    // Existe um trigger para setar a ordem automática.
    @Column(name = "ordem", nullable = false)
    private Long ordem;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_casal", referencedColumnName = "id")
    private Usuario casal;
}