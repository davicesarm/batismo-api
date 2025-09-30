package com.davicesar.batismo.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Catecumeno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_batizado", nullable = false)
    private Batizado batizado;

}
