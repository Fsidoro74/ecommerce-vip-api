package org.example.controller;

import org.example.model.Usuario; // Importar a entidade Usuario
import org.example.repository.UsuarioRepository;
import org.example.model.LoginRequest; // Importar o DTO de login
import org.example.model.RegisterRequest; // **IMPORTANTE: Vamos precisar deste DTO**
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder; // Para codificar a senha
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth") // Endpoint base: /auth
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Injetar o Encoder do SecurityConfig

    // Endpoint: POST /auth/register
    // Permite que novos usuários se cadastrem (este endpoint está liberado no SecurityConfig)
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {

        // 1. Verificar se o login já existe
        if (usuarioRepository.findByLogin(registerRequest.login()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Login já em uso.");
        }

        // 2. Criar e codificar a senha
        String senhaCodificada = passwordEncoder.encode(registerRequest.senha());

        Usuario novoUsuario = new Usuario(
                registerRequest.login(),
                senhaCodificada // Salva a senha criptografada!
        );

        // 3. Salvar o usuário no banco
        usuarioRepository.save(novoUsuario);

        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário registrado com sucesso.");
    }

    // --- O endpoint de LOGIN virá em seguida (será mais complexo) ---
    // @PostMapping("/login")
    // ...
}
