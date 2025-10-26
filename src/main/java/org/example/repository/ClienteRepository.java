package org.example.repository;

import org.example.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Indica ao Spring que esta é uma interface de repositório
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // JpaRepository<Cliente, Long>
    // 1º Argumento: A entidade (Cliente)
    // 2º Argumento: O tipo da chave primária (Long)

    // Métodos customizados que o Spring vai criar automaticamente
    // baseados no nome dos atributos da classe Cliente.
    // Serão úteis no Service para checar duplicatas.

    /**
     * Busca um cliente pelo CPF.
     * @param cpf O CPF a ser buscado.
     * @return um Optional contendo o Cliente, se encontrado.
     */
    Optional<Cliente> findByCpf(String cpf);

    /**
     * Busca um cliente pelo Email.
     * @param email O Email a ser buscado.
     * @return um Optional contendo o Cliente, se encontrado.
     */
    Optional<Cliente> findByEmail(String email);
}
