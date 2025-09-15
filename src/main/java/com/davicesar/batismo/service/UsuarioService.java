package com.davicesar.batismo.service;

import com.davicesar.batismo.dto.login.LoginRequest;
import com.davicesar.batismo.dto.login.LoginResponse;
import com.davicesar.batismo.dto.usuario.CadastroUsuarioDTO;
import com.davicesar.batismo.dto.usuario.UsuarioDTO;
import com.davicesar.batismo.model.Usuario;
import com.davicesar.batismo.repository.UsuarioRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

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

    public List<UsuarioDTO> listarUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioDTO::new)
                .toList();
    }

    public void cadastrarUsuario(CadastroUsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario(usuarioDTO, bCryptPasswordEncoder);
        usuarioRepository.save(usuario);
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
        System.out.println(scope);
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
}
