package org.example.repository;

import org.example.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // JpaRepository<Usuario, Long>
    // 1º Argumento: A entidade (Usuario)
    // 2º Argumento: O tipo da chave primária (Long)

    /**
     * Método customizado que o Spring Security usará para
     * encontrar um usuário pelo seu nome de login.
     * O Spring Data JPA cria a consulta automaticamente.
     */
    Optional<Usuario> findByLogin(String login);
}