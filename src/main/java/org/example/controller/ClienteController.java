package org.example.controller;

import jakarta.validation.Valid;
import org.example.model.Cliente;
import org.example.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/clientes") // Endpoint base para todos os métodos desta classe
public class ClienteController {

    @Autowired // Injeta o Service de Cliente
    private ClienteService clienteService;

    // Endpoint: GET /clientes
    // Retorna todos os clientes
    @GetMapping
    public ResponseEntity<List<Cliente>> listarTodos() {
        List<Cliente> clientes = clienteService.listarTodos();
        return ResponseEntity.ok(clientes); // 200 OK
    }

    // Endpoint: GET /clientes/{id}
    // Retorna um cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        return clienteService.buscarPorId(id)
                             .map(cliente -> ResponseEntity.ok(cliente)) // 200 OK
                             .orElse(ResponseEntity.notFound().build()); // 404 Not Found
    }

    // Endpoint: POST /clientes
    // Cria um novo cliente
    @PostMapping
    public ResponseEntity<Cliente> criar(@Valid @RequestBody Cliente cliente) {
        // @Valid aciona as validações (@NotBlank, @Email, etc.)
        try {
            Cliente clienteSalvo = clienteService.salvar(cliente);

            // Retorna 201 Created + URL do novo recurso
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                                      .path("/{id}")
                                                      .buildAndExpand(clienteSalvo.getId())
                                                      .toUri();

            return ResponseEntity.created(location).body(clienteSalvo);
        } catch (RuntimeException e) {
            // Se o Service lançar exceção (CPF/Email duplicado)
            // O ideal seria um @ExceptionHandler, mas por agora 400 Bad Request funciona
            return ResponseEntity.badRequest().body(null); // 400 Bad Request
        }
    }

    // Endpoint: PUT /clientes/{id}
    // Atualiza completamente um cliente
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @Valid @RequestBody Cliente cliente) {
        try {
            Cliente clienteAtualizado = clienteService.atualizar(id, cliente);
            return ResponseEntity.ok(clienteAtualizado); // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    // Endpoint: PATCH /clientes/{id}
    // Atualiza parcialmente um cliente
    @PatchMapping("/{id}")
    public ResponseEntity<Cliente> atualizarParcial(@PathVariable Long id, @RequestBody Cliente clienteParcial) {
        try {
            Cliente clienteAtualizado = clienteService.atualizarParcial(id, clienteParcial);
            return ResponseEntity.ok(clienteAtualizado); // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    // Endpoint: DELETE /clientes/{id}
    // Deleta um cliente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            clienteService.deletar(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}
