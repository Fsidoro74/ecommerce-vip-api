package org.example.controller;

import jakarta.validation.Valid;
import org.example.model.Produto;
import org.example.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController // Define que esta classe é um Controller REST
@RequestMapping("/produtos") // Todas as requisições para "/produtos" cairão aqui
public class ProdutoController {

    @Autowired // Injeta o nosso Service
    private ProdutoService produtoService;

    // Endpoint: GET /produtos
    // Retorna todos os registros
    @GetMapping
    public ResponseEntity<List<Produto>> listarTodos() {
        List<Produto> produtos = produtoService.listarTodos();
        return ResponseEntity.ok(produtos); // Retorna 200 OK + lista de produtos
    }

    // Endpoint: GET /produtos/{id}
    // Retorna um registro por ID
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        // Usa o .map() do Optional:
        // Se encontrar, retorna 200 OK com o produto
        // Se não encontrar, retorna 404 Not Found
        return produtoService.buscarPorId(id)
                             .map(produto -> ResponseEntity.ok(produto))
                             .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint: POST /produtos
    // Cria um novo registro
    @PostMapping
    public ResponseEntity<Produto> criar(@Valid @RequestBody Produto produto) {
        // @Valid: Aciona as validações (@NotBlank, etc.) da entidade Produto
        // @RequestBody: Pega o JSON do corpo da requisição e converte para um objeto Produto

        Produto produtoSalvo = produtoService.salvar(produto);

        // Boa prática REST: Retornar 201 Created + a URL do novo recurso
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                                  .path("/{id}")
                                                  .buildAndExpand(produtoSalvo.getId())
                                                  .toUri();

        return ResponseEntity.created(location).body(produtoSalvo);
    }

    // Endpoint: PUT /produtos/{id}
    // Atualiza completamente um registro
    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @Valid @RequestBody Produto produto) {
        try {
            Produto produtoAtualizado = produtoService.atualizar(id, produto);
            return ResponseEntity.ok(produtoAtualizado); // Retorna 200 OK + produto atualizado
        } catch (RuntimeException e) {
            // Se o service lançar a exceção "Produto não encontrado"
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint: PATCH /produtos/{id}
    // Atualiza parcialmente um registro
    @PatchMapping("/{id}")
    public ResponseEntity<Produto> atualizarParcial(@PathVariable Long id, @RequestBody Produto produtoParcial) {
        try {
            Produto produtoAtualizado = produtoService.atualizarParcial(id, produtoParcial);
            return ResponseEntity.ok(produtoAtualizado); // Retorna 200 OK + produto atualizado
        } catch (RuntimeException e) {
            // Se o service lançar a exceção "Produto não encontrado"
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint: DELETE /produtos/{id}
    // Deleta um registro
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            produtoService.deletar(id);
            return ResponseEntity.noContent().build(); // Retorna 204 No Content (sucesso sem corpo)
        } catch (RuntimeException e) {
            // Se o service lançar a exceção "Produto não encontrado"
            return ResponseEntity.notFound().build();
        }
    }
}