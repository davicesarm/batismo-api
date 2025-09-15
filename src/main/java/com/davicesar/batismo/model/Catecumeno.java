package com.davicesar.batismo.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Catecumeno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "id_batizado", nullable = false)
    private Batizado batizado;

}
