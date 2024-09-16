package com.spring.security.controller;

import com.spring.security.entity.Usuario;
import com.spring.security.enums.Perfil;
import com.spring.security.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private UsuarioService usuarioService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> criarUsuario(@RequestBody Usuario usuario) {
        usuarioService.criarUsuario(usuario);
        return ResponseEntity.ok().build();
    }

}
