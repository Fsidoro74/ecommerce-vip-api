package org.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do produto é obrigatório.")
    @Size(min = 3, message = "O nome deve ter no mínimo 3 caracteres.")
    private String nome;

    @NotBlank(message = "A descrição é obrigatória.")
    @Column(length = 500) // Define o tamanho máximo no banco
    private String descricao;

    @NotBlank(message = "O SKU é obrigatório.")
    @Column(unique = true) // Garante que não haverá SKUs repetidos
    private String sku;

    @Positive(message = "O preço deve ser maior que zero.")
    private Double preco; // Usamos Double (classe Wrapper)

    @Min(value = 0, message = "A quantidade em estoque não pode ser negativa.")
    private Integer quantidadeEstoque; // Usamos Integer (classe Wrapper)

    // Construtor vazio (Obrigatório para o JPA)
    public Produto() {
    }

    // Construtor com campos (útil para testes)
    public Produto(String nome, String descricao, String sku, Double preco, Integer quantidadeEstoque) {
        this.nome = nome;
        this.descricao = descricao;
        this.sku = sku;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    // --- Getters e Setters ---
    // (O Spring/JPA precisa deles para acessar os campos)

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Integer getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(Integer quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }
}
