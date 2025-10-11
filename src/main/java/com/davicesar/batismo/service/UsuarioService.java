package com.davicesar.batismo.service;

import com.davicesar.batismo.dto.login.LoginRequest;
import com.davicesar.batismo.dto.login.RedefinirSenhaDTO;
import com.davicesar.batismo.dto.usuario.UsuarioProfileDTO;
import com.davicesar.batismo.dto.usuario.UsuarioRequest;
import com.davicesar.batismo.dto.usuario.UsuarioResponse;
import com.davicesar.batismo.exception.OperacaoProibidaException;
import com.davicesar.batismo.exception.ProcessamentoInvalidoException;
import com.davicesar.batismo.exception.UsuarioNaoEncontradoException;
import com.davicesar.batismo.model.Cargo;
import com.davicesar.batismo.model.Usuario;
import com.davicesar.batismo.repository.OrdemCasalRepository;
import com.davicesar.batismo.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtEncoder jwtEncoder;
    private final EmailService emailService;
    private final OrdemCasalRepository ordemCasalRepository;
    private final CookieUtil cookieUtil;

    public UsuarioService(
            JwtEncoder jwtEncoder,
            UsuarioRepository usuarioRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            EmailService emailService,
            OrdemCasalRepository ordemCasalRepository,
            CookieUtil cookieUtil
    ) {
        this.jwtEncoder = jwtEncoder;
        this.usuarioRepository = usuarioRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailService = emailService;
        this.ordemCasalRepository = ordemCasalRepository;
        this.cookieUtil = cookieUtil;
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
                () -> new UsuarioNaoEncontradoException(id)
        );

        Cargo cargo = usuario.getCargo();
        if (!cargo.name().equals(dto.cargo().name())) {
            throw new OperacaoProibidaException("Não é possível alterar o cargo.");
        }

        usuarioRepository.findByEmail(dto.email())
                .ifPresent(u -> {
                    throw new ProcessamentoInvalidoException("Usuário com email: " + dto.email() + " já existe.");
                });


        try {
            usuario.setEmail(dto.email());
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
                () -> new UsuarioNaoEncontradoException(id)
        );

        if (usuario.getCargo() == Cargo.casal && ordemCasalRepository.findAll().size() <= 1) {
            throw new OperacaoProibidaException("A quantidade de casais é não pode ser menor que 1");
        }

        usuario.setInativo(true);
    }

    @Transactional
    public void reativarUsuario(Long id) {
        var usuario = usuarioRepository.findById(id).orElseThrow(
                () -> new UsuarioNaoEncontradoException(id)
        );

        usuario.setInativo(false);
    }

    @Transactional
    public void cadastrarUsuario(UsuarioRequest usuarioDTO) {
        usuarioRepository.findByEmail(usuarioDTO.email())
                .ifPresentOrElse(
                        usuario -> reativarUsuario(usuario, usuarioDTO),
                        () -> {
                            Usuario novoUsuario = new Usuario(usuarioDTO);
                            String token = TokenUtil.generateToken();
                            novoUsuario.setSenha(token, bCryptPasswordEncoder);
                            novoUsuario.setSenha_alterada(false);
                            usuarioRepository.save(novoUsuario);
                            // Coloca isso em uma tread separada...
                            emailService.enviarEmailDeVerificacao(novoUsuario.getEmail(), token);
                        }
                );
    }

    @Transactional
    public ResponseCookie redefinirSenha(RedefinirSenhaDTO dto, Long userId){
        var usuario = usuarioRepository.findById(userId).orElseThrow(
                () -> new UsuarioNaoEncontradoException(userId)
        );

        if (!usuario.loginCorreto(dto.senhaAntiga(), bCryptPasswordEncoder)) {
            throw new BadCredentialsException("Senha incorreta.");
        }

        usuario.setSenha(dto.senhaNova(), bCryptPasswordEncoder);
        usuario.setSenha_alterada(true);
        String jwt = generateJwt(usuario);
        return cookieUtil.generateJwtCookie(jwt);
    }

    public ResponseCookie autenticarUsuario(LoginRequest loginRequest) {
        var usuario = usuarioRepository.findByEmail(loginRequest.email());

        if (usuario.isEmpty() || usuario.get().isInativo() || !usuario.get().loginCorreto(loginRequest.senha(), bCryptPasswordEncoder)) {
            throw new BadCredentialsException("Email ou senha incorretos.");
        }
        String jwt = generateJwt(usuario.get());
        return cookieUtil.generateJwtCookie(jwt);
    }

    private String generateJwt(Usuario usuario) {
        var now = Instant.now();

        // TODO: implementar refresh token
        var expiresIn = 3600 * 24; // 1 dia

        var scope = usuario.getCargo().name();
        if (!usuario.isSenha_alterada()) {
            scope = "redefinir-senha";
        }

        var claims = JwtClaimsSet.builder()
                .issuer("batismo-api")
                .subject(usuario.getId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", scope)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private void reativarUsuario(Usuario usuario, UsuarioRequest dto) {
        if (!usuario.isInativo()) {
            throw new ProcessamentoInvalidoException("O usuário já está ativo/cadastrado.");
        }

        usuario.setInativo(false);

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

    public UsuarioProfileDTO getDadosUsuario(Long id) {
        var usuario = usuarioRepository.findById(id).orElseThrow(
                () -> new UsuarioNaoEncontradoException(id)
        );

        return new UsuarioProfileDTO(usuario.getEmail(), usuario.getCargo());
    }
}
