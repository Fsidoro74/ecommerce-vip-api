package org.example.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Relacionamento: Muitos Pedidos para Um Cliente ---
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false) // Chave estrangeira
    private Cliente cliente;

    // --- Relacionamento: Muitos Pedidos para Muitos Produtos ---
    @ManyToMany
    @JoinTable(
            name = "pedido_produtos", // Nome da tabela de junção
            joinColumns = @JoinColumn(name = "pedido_id"), // Chave deste lado
            inverseJoinColumns = @JoinColumn(name = "produto_id") // Chave do outro lado
    )
    private List<Produto> produtos;

    @Column(nullable = false)
    private LocalDate dataPedido;

    @Column(nullable = false)
    private Double valorTotal;

    @Column(nullable = false)
    private String status; // Ex: "PROCESSANDO", "ENVIADO", "ENTREGUE"

    // Construtor vazio
    public Pedido() {
    }

    // --- Getters e Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    public LocalDate getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDate dataPedido) {
        this.dataPedido = dataPedido;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}