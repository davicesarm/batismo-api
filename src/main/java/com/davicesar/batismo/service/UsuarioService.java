package com.davicesar.batismo.service;

import com.davicesar.batismo.dto.login.LoginRequest;
import com.davicesar.batismo.dto.login.LoginResponse;
import com.davicesar.batismo.dto.usuario.UsuarioRequest;
import com.davicesar.batismo.dto.usuario.UsuarioResponse;
import com.davicesar.batismo.model.Cargo;
import com.davicesar.batismo.model.Usuario;
import com.davicesar.batismo.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtEncoder jwtEncoder;

    public UsuarioService(JwtEncoder jwtEncoder, UsuarioRepository usuarioRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.usuarioRepository = usuarioRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public List<UsuarioResponse> listarUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioResponse::new)
                .toList();
    }

    @Transactional
    public void editarUsuario(Long id, UsuarioRequest dto) {
        var usuario = usuarioRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY));

        Cargo cargo = usuario.getCargo();
        if (!cargo.name().equals(dto.cargo().name())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        try {
            usuario.setEmail(dto.email());
            if (dto.senha() != null) {
                usuario.setSenha(dto.senha(), bCryptPasswordEncoder);
            }
            if (cargo != Cargo.casal) {
                usuario.setNome(dto.nome());
            } else {
                usuario.setMarido(dto.marido());
                usuario.setMulher(dto.mulher());
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public void inativarUsuario(Long id) {
        var usuario = usuarioRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY));

        usuario.setInativo(true);
    }

    @Transactional
    public void reativarUsuario(Long id) {
        var usuario = usuarioRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY));

        usuario.setInativo(false);
    }

    @Transactional
    public void cadastrarUsuario(UsuarioRequest usuarioDTO) {
        usuarioRepository.findByEmail(usuarioDTO.email())
                .ifPresentOrElse(
                        usuario -> reativarUsuario(usuario, usuarioDTO),
                        () -> {
                            Usuario novoUsuario = new Usuario(usuarioDTO, bCryptPasswordEncoder);
                            usuarioRepository.save(novoUsuario);
                        }
                );
    }

    public LoginResponse autenticarUsuario(LoginRequest loginRequest) {
        var usuario = usuarioRepository.findByEmail(loginRequest.email());

        if (usuario.isEmpty() || !usuario.get().loginCorreto(loginRequest, bCryptPasswordEncoder)) {
            throw new BadCredentialsException("email ou senha incorretos.");
        }

        var now = Instant.now();

        // TODO: implementar refresh token
        var expiresIn = 86400L; // 1 dia

        var scope = usuario.get().getCargo().name();

        var claims = JwtClaimsSet.builder()
                .issuer("batismo-api")
                .subject(usuario.get().getId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", scope)
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new LoginResponse(jwtValue, expiresIn);
    }

    private void reativarUsuario(Usuario usuario, UsuarioRequest dto) {
        if (!usuario.isInativo()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        usuario.setInativo(false);
        usuario.setSenha(dto.senha(), bCryptPasswordEncoder);

        try {
            if (usuario.getCargo() != Cargo.casal) {
                usuario.setNome(dto.nome());
                return;
            }

            usuario.setMulher(dto.mulher());
            usuario.setMarido(dto.marido());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
