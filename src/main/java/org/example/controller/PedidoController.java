package org.example.controller;

import org.example.model.Pedido;
import org.example.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pedidos") // Endpoint base
public class PedidoController {

    @Autowired // Injeta o Service de Pedido
    private PedidoService pedidoService;

    // Endpoint: GET /pedidos
    // Retorna todos os pedidos
    @GetMapping
    public ResponseEntity<List<Pedido>> listarTodos() {
        List<Pedido> pedidos = pedidoService.listarTodos();
        return ResponseEntity.ok(pedidos); // 200 OK
    }

    // Endpoint: GET /pedidos/{id}
    // Retorna um pedido por ID
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        return pedidoService.buscarPorId(id)
                            .map(pedido -> ResponseEntity.ok(pedido)) // 200 OK
                            .orElse(ResponseEntity.notFound().build()); // 404 Not Found
    }

    // Endpoint: GET /pedidos/por-cliente/{clienteId}
    // Retorna todos os pedidos de um cliente específico
    @GetMapping("/por-cliente/{clienteId}")
    public ResponseEntity<List<Pedido>> buscarPorCliente(@PathVariable Long clienteId) {
        List<Pedido> pedidos = pedidoService.buscarPorClienteId(clienteId);
        return ResponseEntity.ok(pedidos); // 200 OK
    }

    // Endpoint: POST /pedidos
    // Cria um novo pedido. Este é diferente!
    // Não recebemos um Pedido, mas sim os IDs para criar um.
    @PostMapping
    public ResponseEntity<Pedido> criar(
            @RequestParam Long clienteId,
            @RequestBody List<Long> produtoIds) {

        try {
            Pedido pedidoSalvo = pedidoService.criar(clienteId, produtoIds);

            // Retorna 201 Created + URL do novo recurso
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                                      .path("/{id}")
                                                      .buildAndExpand(pedidoSalvo.getId())
                                                      .toUri();

            return ResponseEntity.created(location).body(pedidoSalvo);
        } catch (RuntimeException e) {
            // Se o Service lançar exceção (ex: Cliente não encontrado, Produto sem estoque)
            return ResponseEntity.badRequest().body(null); // 400 Bad Request
        }
    }

    // Endpoint: PATCH /pedidos/{id}/status
    // Atualiza apenas o status de um pedido
    @PatchMapping("/{id}/status")
    public ResponseEntity<Pedido> atualizarStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusUpdate) {

        try {
            String novoStatus = statusUpdate.get("status");
            if (novoStatus == null) {
                return ResponseEntity.badRequest().body(null); // 400 Bad Request
            }

            Pedido pedidoAtualizado = pedidoService.atualizarStatus(id, novoStatus);
            return ResponseEntity.ok(pedidoAtualizado); // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    // Endpoint: DELETE /pedidos/{id}
    // Deleta (ou cancela) um pedido
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            pedidoService.deletar(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}
