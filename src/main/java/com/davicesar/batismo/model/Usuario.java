package com.davicesar.batismo.model;

import com.davicesar.batismo.dto.usuario.CadastroUsuarioDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 254)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    private Cargo cargo;

    private String nome;

    private String marido;

    private String mulher;

    public Usuario(CadastroUsuarioDTO user){
        this.email = user.email();
        this.senha = user.senha();
        this.cargo = Cargo.valueOf(user.cargo());
        if (cargo.equals(Cargo.casal)) {
            this.marido = user.marido();
            this.mulher = user.mulher();
        } else {
            this.nome = user.nome();
        }
    }
}

