package org.example.repository;

import org.example.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Indica ao Spring que esta é uma interface de repositório
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // JpaRepository<Produto, Long>
    // 1º Argumento: A entidade que este repositório vai gerenciar (Produto)
    // 2º Argumento: O tipo da chave primária (ID) da entidade (Long)

    // O Spring Data JPA cria automaticamente todos os métodos CRUD para nós.

    // Método customizado:
    // Se seguirmos a convenção de nomenclatura "findBy[NomeDoAtributo]",
    // o Spring automaticamente cria a consulta para nós.
    // Aqui, ele vai procurar um Produto pelo atributo 'sku'.
    Optional<Produto> findBySku(String sku);
}
