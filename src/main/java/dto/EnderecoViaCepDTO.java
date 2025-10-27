package org.example.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Um "record" é uma classe Java moderna para guardar dados.
// @JsonIgnoreProperties(ignoreUnknown = true) diz ao Jackson (conversor JSON):
// "Se o JSON do ViaCEP tiver campos que eu não listei aqui (como 'gia', 'ddd', etc.),
// apenas ignore-os em vez de dar um erro."
@JsonIgnoreProperties(ignoreUnknown = true)
public record EnderecoViaCepDTO(
        String logradouro,
        String bairro,
        String localidade,
        String uf
) {
    // É só isso. Um record não precisa de getters, setters ou construtores.
}