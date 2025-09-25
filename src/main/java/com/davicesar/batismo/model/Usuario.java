package com.davicesar.batismo.model;

import com.davicesar.batismo.dto.login.LoginRequest;
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

    public Usuario(UsuarioRequest user, PasswordEncoder passwordEncoder){
        this.email = user.email();
        this.senha = passwordEncoder.encode(user.senha());
        this.cargo = user.cargo();
        if (cargo.equals(Cargo.casal)) {
            this.marido = user.marido();
            this.mulher = user.mulher();
        } else {
            this.nome = user.nome();
        }
    }

    public boolean loginCorreto(LoginRequest loginRequest, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(loginRequest.senha(), this.senha);
    }
}

