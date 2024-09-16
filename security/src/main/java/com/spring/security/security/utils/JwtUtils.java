package com.spring.security.security.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.spring.security.entity.Usuario;
import com.spring.security.security.service.AutenticacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final AutenticacaoService autenticacaoService;

    @Value("${security.secret:SenhaPadraoSeNaoForEncontradaAPropriedade}")
    private String senha;

    public String criarToken(Usuario usuario ){

        Instant instanteDeAssinatura = Instant.now();
        Instant instanteDeExpiracao = instanteDeAssinatura.plus(1, ChronoUnit.HOURS);

        String[] authorities = usuario.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority()).toArray(String[]::new);

        String jwt = JWT.create().withIssuer("Top Care")
                .withIssuedAt(instanteDeAssinatura)
                .withSubject(usuario.getUsuario())
                .withExpiresAt(instanteDeExpiracao)
                .withArrayClaim("authorities", authorities)
                .sign(Algorithm.HMAC256(senha));

        return jwt;

    }

    public Authentication validarToken(String token) {
        String username = JWT.require(Algorithm.HMAC256(senha))
                .build()
                .verify(token)
                .getSubject();
//        String username = JWT.decode(token).getSubject();
        Usuario usuario = (Usuario) autenticacaoService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(usuario, usuario.getPassword(), usuario.getAuthorities());
    }
}
