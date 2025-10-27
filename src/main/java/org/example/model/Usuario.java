package org.example.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails { // Implementar UserDetails é crucial

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String login; // O "username"
    private String senha; // A senha (vamos salvar ela codificada)

    // Construtores
    public Usuario() {}

    public Usuario(String login, String senha) {
        this.login = login;
        this.senha = senha;
    }

    // --- Getters e Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }


    // --- Métodos obrigatórios do UserDetails ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Por simplicidade, vamos dizer que todo usuário é um "USER"
        // Em um app real, isso também viria do banco
        return List.of(() -> "ROLE_USER");
    }

    @Override
    public String getPassword() {
        return this.senha; // O Spring Security usará isso
    }

    @Override
    public String getUsername() {
        return this.login; // O Spring Security usará isso
    }

    // Para este projeto simples, podemos deixar todos como 'true'
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}