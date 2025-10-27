package org.example.ecommercevip.model;

// Record simples para receber o JSON do registro (login e senha)
public record RegisterRequest(
        String login,
        String senha
) {
}
