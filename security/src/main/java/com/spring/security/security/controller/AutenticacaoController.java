package com.spring.security.security.controller;

import com.spring.security.entity.Usuario;
import com.spring.security.security.controller.dto.LoginDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    private AuthenticationProvider authenticationManager;
    private SecurityContextRepository securityContextRepository;

    @PreAuthorize("permitAll()")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto login, HttpServletRequest request, HttpServletResponse response) {

        //criando um objeto para q seja possivel passar pela autenticacao. Essa é uma
        //interface própria para isso! Ele precisa ser transformado para dar certo, ser
        //interpretado como user details.
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                login.usuario(), login.senha());

        //O manager conhece o provider que conhece os peões que vão implementar os metodos
        authentication = authenticationManager.authenticate(authentication);

        if (authentication.isAuthenticated()) {
            //ISSO TUDO ta presistindo a sessão do usuário, se n tiver, n vai funcionar
            //nada. Fica naquela request que enviou, na próximas n vai mais. Faz com que
            //o objeto de usuário permaneca acordado para as qequests
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);
            securityContextRepository.saveContext(securityContext, request, response);
        }
        return ResponseEntity.ok((Usuario)authentication.getPrincipal());
    }

}
