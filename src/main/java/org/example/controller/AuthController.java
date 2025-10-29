package org.example.controller;

import org.example.model.Usuario; // Importar a entidade Usuario
import org.example.repository.UsuarioRepository;
import org.example.model.LoginRequest; // Importar o DTO de login
import org.example.model.RegisterRequest; // **IMPORTANTE: Vamos precisar deste DTO**
import org.example.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder; // Para codificar a senha
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth") // Endpoint base: /auth
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Injetar o Encoder do SecurityConfig

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

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

    // Endpoint: POST /auth/login
    // Autentica o usuário e retorna um JWT
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // 1) Autenticar as credenciais
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        // 2) Recuperar o principal como UserDetails
        Object principal = auth.getPrincipal();
        if (!(principal instanceof UserDetails userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falha na autenticação.");
        }

        // 3) Gerar o token com o username como subject
        String token = tokenService.generateToken(userDetails);

        // 4) Retornar em JSON simples
        return ResponseEntity.ok(Map.of("token", token));
    }
}
