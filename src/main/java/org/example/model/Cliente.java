package org.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório.")
    @Size(min = 3, message = "O nome deve ter no mínimo 3 caracteres.")
    private String nome;

    @NotBlank(message = "O CPF é obrigatório.")
    @Size(min = 11, max = 11, message = "O CPF deve conter 11 dígitos.")
    @Column(unique = true, length = 11) // Garante CPF único no banco
    private String cpf;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Formato de e-mail inválido.")
    @Column(unique = true) // Garante e-mail único no banco
    private String email;

    @NotNull(message = "O status VIP não pode ser nulo.")
    @Column(name = "is_vip") // Boa prática para nomes de colunas booleanas
    private Boolean isVip = false; // Valor padrão 'false'

    // --- NOVO CAMPO ---
    @NotBlank(message = "O CEP é obrigatório.")
    @Size(min = 8, max = 8, message = "O CEP deve conter 8 dígitos (apenas números).")
    private String cep;
    // ------------------


    // Construtor vazio (Obrigatório para o JPA)
    public Cliente() {
    }

    // Construtor com campos (agora com o 'cep')
    public Cliente(String nome, String cpf, String email, Boolean isVip, String cep) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.isVip = isVip;
        this.cep = cep; // Adicionado
    }

    // --- Getters e Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsVip() {
        return isVip;
    }

    public void setIsVip(Boolean isVip) {
        this.isVip = isVip;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }
}