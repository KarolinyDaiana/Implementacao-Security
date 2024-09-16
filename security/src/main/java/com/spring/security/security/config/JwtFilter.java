package com.spring.security.security.config;


import com.spring.security.entity.Usuario;
import com.spring.security.security.utils.CookieUtils;
import com.spring.security.security.utils.JwtUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
@AllArgsConstructor
//podia usar o filter, mas precisaria verificar se é um httpServerRequest
public class JwtFilter extends OncePerRequestFilter {

    private CookieUtils cookieUtils;
    private JwtUtils jwtUtils;

    // O OBJTIVO É: VER SE O USUÁRIO JÁ FOI
    @Override
    public void doFilterInternal(HttpServletRequest request,
                         HttpServletResponse response,
                         FilterChain filterChain)
            throws IOException, ServletException {

        String uri = request.getRequestURI();
        String method = request.getMethod();
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        if (!isPublicEndpoint(uri, method)) {
            try {
//        request.getRequestURI();  // n tem dominio, é local
                //se n tem o setAllowCredentials(true) no securityConfig ele n consegue achar
                //no front, se nao for publico, tem que colocar withAloowedCredentials(true) eu acho
                Cookie[] cookies = request.getCookies();
                Optional<Cookie> cookieOptional = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("USERTOKEN")).findFirst();

                    if (cookieOptional.isPresent()) {
                // request.getHeader("Authentication"); Alternativa qu retorna uma string, mas precisa
                // remover os primerios caracteres (Bearer)
                        Cookie cookie = cookieOptional.get();
                        String token = cookie.getValue();
                        Authentication authentication = jwtUtils.validarToken(token);

                        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                        securityContext.setAuthentication(authentication);
                        SecurityContextHolder.setContext(securityContext);

                        if (!uri.equals("/auth/logout")) {
                            String novoToken = jwtUtils.criarToken((Usuario) authentication.getPrincipal());
                            cookie = cookieUtils.criarCookie(novoToken);
                            response.addCookie(cookie);
                        }
                    }
                } catch (Exception e) {
                    response.setStatus(401);
                    return;
            }
        }
        filterChain.doFilter(request, response);
    }
    private boolean isPublicEndpoint(String uri, String method) {
        return uri.equals("/auth/login") && method.equals("POST");
    }
}

