package com.spring.security.security.config;

import com.spring.security.security.service.AutenticacaoService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(AutenticacaoService autenticacaoService) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(autenticacaoService);
        authenticationProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity config) throws Exception {
        config.securityContext(custom -> securityContextRepository());
        config.formLogin( custom -> custom.disable());
//        config.httpBasic( custom -> custom.disable());
//        ALGO SIMILAR AO FORM LOGIN, MAS É EM FORMATO DE ALERT
        config.logout(custom -> custom.disable());
//        config.authorizeHttpRequests(http -> {
//            http.requestMatchers(HttpMethod.POST, "/auth/login").permitAll();
//            http.anyRequest().authenticated();
//                .access(new AuthorizationManager<RequestAuthorizationContext>() {
//                @Override
//                public AuthorizationDecision check(
//                        Supplier<Authentication> authentication, RequestAuthorizationContext object) {
//                    authentication.get().getPrincipal();
//                    return new AuthorizationDecision();
//                }
//            });
//            http.requestMatchers(HttpMethod.POST, "/auth/logout").authenticated();
//            .hasAnyRole() e .hasAuthority()
//        });
        config.csrf(custom -> custom.disable());
        return config.build();
    }

    // Metpdp cpmstrutor n é pra usar em min; O construtor;
//    @Bean
//    public AuthenticationManager authenticationManager(
//            //É necessário um orquestrador por conta de vários provedores.
//            //Ele sabe O QUE fazer, os providers vao dizer como.
//            AutenticacaoService autenticacaoService) {
//
//        //Isso é um provedor de autenticação, ele tem os meios para busca e decriptacao
//        //de senhas.
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(autenticacaoService);
//        authProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
//        return authProvider
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
//        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public UserDetailsService authenticationService() {
//        List<Usuario> usuarios = usuarioRepository.findAll();
//        List<UserDetails> users = new ArrayList<>(usuarios);
//
//        return new InMemoryUserDetailsManager(users); //em memoria, se mudar um usuário,
//                                                     // ele n consegue ver a atualização
//        UserDetails user = User.builder()
//                .username("admin")
//                .password(passwordEncoder()
//                        .encode("Sena!123"))
//                .build();
//        UserDetails user2 = User.builder()
//                .username("funcionario")
//                .password(passwordEncoder()
//                        .encode("Sena!123"))
//                .build();
//    }

}
