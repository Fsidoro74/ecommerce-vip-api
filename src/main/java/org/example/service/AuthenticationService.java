package org.example.service;

import org.example.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service // Indica ao Spring que é um serviço
public class AuthenticationService implements UserDetailsService {
    // 1. Implementamos a interface que o Spring Security entende

    @Autowired
    private UsuarioRepository usuarioRepository; // 2. Injetamos nosso repositório

    /**
     * Este é o método que o Spring Security vai chamar
     * automaticamente quando um usuário tentar fazer login.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 3. Usamos nosso repositório para buscar o usuário pelo 'login'
        return usuarioRepository.findByLogin(username)
                                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }
}