package com.spring.security.entity;

import com.spring.security.enums.Perfil;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String usuario;
    private String senha;
    private String email;
    private Perfil perfil;
//    private boolean habilitado = true;
//    private boolean habilitado;  O GetAthorities
//    private boolean senhaExpirada;
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return !this.senhaExpirada;
//    }

//    @Override
//    public Collection<? extends GrantedAuthority("")> getAuthorities(){
//        return List.of(new SimpleGrantedAuthority(
//                UsuarioCenarioDois.class.getSimpleName()
//        ));
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(perfil);
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return this.usuario;
    }

    public Usuario(String nome, String usuario, String senha, String email) {
        this.nome = nome;
        this.usuario = usuario;
        this.senha = senha;
        this.email = email;
    }
}
