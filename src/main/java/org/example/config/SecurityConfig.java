package org.example.config;

import org.example.service.AuthenticationService; // Importar o nosso Service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Novo
import org.springframework.security.crypto.password.PasswordEncoder; // Novo
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity // Habilita a segurança
public class SecurityConfig {

    @Autowired
    private AuthenticationService authenticationService; // Injetar nosso Service

    @Autowired
    private SecurityFilter securityFilter; // Nosso filtro JWT

    // 1. Configurar o Encoder (BCrypt é o padrão e mais seguro)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Configurar o Authentication Manager (Gerenciador de Autenticação)
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        // Diz ao Spring para usar o nosso AuthenticationService
        authenticationManagerBuilder.userDetailsService(authenticationService)
                                    .passwordEncoder(passwordEncoder());

        return authenticationManagerBuilder.build();
    }


    // 3. Configurar as Regras de Filtro HTTP (quem pode acessar o quê)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desativa a proteção CSRF, que não é necessária para a API REST
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(authorize -> authorize
                        // Permite acesso público aos endpoints de autenticação (POST) e documentação (GET)
                        .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/register").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // Tranca todos os outros endpoints (produtos, clientes, pedidos)
                        .anyRequest().authenticated()
                );

        // Por enquanto, não vamos usar autenticação básica ou formulário
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        // Sessões stateless para API REST
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Registrar nosso filtro JWT antes do filtro padrão de usuário/senha
        http.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
