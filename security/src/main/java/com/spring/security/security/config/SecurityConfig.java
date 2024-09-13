package com.spring.security.security.config;

import com.spring.security.enums.Perfil;
import com.spring.security.security.service.AutenticacaoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@AllArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

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
//        config.securityContext(custom -> securityContextRepository());
//        config.httpBasic( custom -> custom.disable());
//        ALGO SIMILAR AO FORM LOGIN, MAS É EM FORMATO DE ALERT
        config.formLogin( custom -> custom.disable());
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
        // Configuração extra para JWT >>>
        config.sessionManagement(custom -> custom.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        config.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        // <<<
//        config.authorizeHttpRequests(http -> {
//            http.requestMatchers(HttpMethod.POST, "/usuario")
//                    .hasAuthority(Perfil.ADMIN.getAuthority()); //pode colocar ("ADMIN"), mas é ideal dese jeito
//            http.requestMatchers(HttpMethod.POST, "/auth/login")
//                    .permitAll();
//            http.anyRequest().authenticated();
//        });
        config.cors(custom -> custom.configurationSource(corsConfigurationSource()));
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
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.addAllowedOrigin("http://localhost:3000"); se quiser um especifico
        configuration.setAllowedOrigins(List.of("https://top.care", "http://localhost:3000","https://teste.top.care"));
        configuration.setAllowedHeaders(List.of("*")); //qualquer coisa
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

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
