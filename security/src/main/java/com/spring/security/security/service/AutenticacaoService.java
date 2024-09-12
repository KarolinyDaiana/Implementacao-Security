package com.spring.security.security.service;

import com.spring.security.entity.Usuario;
import com.spring.security.entity.cenario_3.PessoaCenarioTres;
import com.spring.security.entity.cenario_3.UsuarioCenarioTres;
import com.spring.security.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AutenticacaoService implements UserDetailsService {

    private UsuarioRepository usuarioRepository;
    //aqui seria o pessoaRepository

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<Usuario> usuario = usuarioRepository.findByUsuario(username);
        Optional<Usuario> usuario = tentarBuscarPorUsuario(username);
        if (usuario.isEmpty()) {
            usuario = tentarBuscarPorEmail(username);
        }
        try {
//            Usuario usuario1 = usuario.get();
            PessoaCenarioTres pessoa = usuario.get(); //seria o pessoa repository
            return new UsuarioCenarioTres(pessoa);
        } catch (NoSuchElementException e) {
            throw new UsernameNotFoundException("O usuário " + username + " não foi encontrado");
        }
    }

    private Optional<Usuario> tentarBuscarPorUsuario(String username) {
        return usuarioRepository.findByUsuario(username);
    }

    private Optional<Usuario> tentarBuscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

}
