package com.davicesar.batismo.model;

import com.davicesar.batismo.dto.usuario.UsuarioRequest;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Column(nullable = false)
    private boolean inativo;

    @Column(nullable = false)
    private boolean senha_alterada;

    public Usuario(UsuarioRequest user){
        this.email = user.email();
        this.cargo = user.cargo();
        if (cargo.equals(Cargo.casal)) {
            this.marido = user.marido();
            this.mulher = user.mulher();
        } else if (!cargo.equals(Cargo.redefinir_senha)) {
            this.nome = user.nome();
        }
    }

    public void setSenha(String senha, PasswordEncoder passwordEncoder) {
        this.senha = passwordEncoder.encode(senha);
    }

    public boolean loginIncorreto(String senha, PasswordEncoder passwordEncoder) {
        return !passwordEncoder.matches(senha, this.senha);
    }
}

