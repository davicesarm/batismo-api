package com.davicesar.batismo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Batizado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private LocalDateTime data;

    private String celebrante;

    @ManyToOne
    @JoinColumn(name = "id_casal", nullable = false)
    private Usuario casal;

    @OneToMany(mappedBy = "batizado", fetch = FetchType.EAGER) // ou LAZY
    private List<Catecumeno> catecumenos;
}
