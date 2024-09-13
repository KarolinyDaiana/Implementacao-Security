package com.spring.security.security.controller;

import com.spring.security.entity.Usuario;
import com.spring.security.security.controller.dto.LoginDto;
import com.spring.security.security.utils.CookieUtils;
import com.spring.security.security.utils.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    private AuthenticationProvider authenticationManager;
    private SecurityContextRepository securityContextRepository;
    private JwtUtils jwtUtils;
    private CookieUtils cookieUtils;

    @PreAuthorize("permitAll()")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto login,
//                                   HttpServletRequest request,
                                   HttpServletResponse response) {

        //criando um objeto para q seja possivel passar pela autenticacao. Essa é uma
        //interface própria para isso! Ele precisa ser transformado para dar certo, ser
        //interpretado como user details.
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                login.usuario(), login.senha());

        //O manager conhece o provider que conhece os peões que vão implementar os metodos
        authentication = authenticationManager.authenticate(authentication);

        if (authentication.isAuthenticated()) {
            Usuario usuario = (Usuario) authentication.getPrincipal();
            String jwt = jwtUtils.criarToken(usuario);
            // Esses dois podem ser retirados em outro modo de fazer
            Cookie cookieJwt = cookieUtils.criarCookie(jwt);
            response.addCookie(cookieJwt);
        }
        return ResponseEntity.ok((Usuario)authentication.getPrincipal());
//        return ResponseEntity.ok((Usuario) authentication.getPrincipal());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user")
    public ResponseEntity<?> usuarioLogado(@AuthenticationPrincipal Usuario usuario) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Usuario usuario = (Usuario) authentication.getPrincipal();
        return ResponseEntity.ok(usuario);
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> login(HttpServletResponse response) {
        Cookie cookie = cookieUtils.removerCookie();
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

}
