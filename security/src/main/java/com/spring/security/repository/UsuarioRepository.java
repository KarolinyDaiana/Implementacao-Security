package com.spring.security.repository;

import com.spring.security.entity.Usuario;
import com.spring.security.entity.cenario_2.UsuarioCenarioDois;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsuario(String usuario);
    Optional<Usuario> findByEmail(String email);
}
