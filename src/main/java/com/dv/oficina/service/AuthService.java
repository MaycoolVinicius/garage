package com.dv.oficina.service;

import com.dv.oficina.dto.request.LoginRequest;
import com.dv.oficina.dto.response.TokenResponse;
import com.dv.oficina.model.Usuario;
import com.dv.oficina.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    @Value("${app.jwt.expiracao-horas}")
    private long expiracaoHoras;

    public TokenResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByUsername(request.username().trim())
                .filter(u -> passwordEncoder.matches(request.senha(), u.getSenhaHash()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos"));

        Instant agora = Instant.now();
        Instant expira = agora.plus(Duration.ofHours(expiracaoHoras));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("oficina")
                .issuedAt(agora)
                .expiresAt(expira)
                .subject(usuario.getUsername())
                .claim("perfil", usuario.getPerfil().name())
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();

        return new TokenResponse(token, expira);
    }
}