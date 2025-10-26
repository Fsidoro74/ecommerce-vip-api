package org.example.repository;

import org.example.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Indica ao Spring que esta é uma interface de repositório
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // JpaRepository<Pedido, Long>
    // 1º Argumento: A entidade (Pedido)
    // 2º Argumento: O tipo da chave primária (Long)

    // --- Métodos Customizados ---
    // O Spring cria a consulta automaticamente pelo nome do método

    /**
     * Busca todos os pedidos associados a um ID de cliente específico.
     * @param clienteId O ID do cliente.
     * @return Uma lista de Pedidos daquele cliente.
     */
    List<Pedido> findByClienteId(Long clienteId);

}
