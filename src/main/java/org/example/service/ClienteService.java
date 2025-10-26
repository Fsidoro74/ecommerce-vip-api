package org.example.service;

import org.example.model.Cliente;
import org.example.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Indica ao Spring que esta é uma classe de serviço
public class ClienteService {

    @Autowired // Injeta o repositório de Cliente
    private ClienteRepository clienteRepository;

    // Método para o GET (todos)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    // Método para o GET (por ID)
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    // Método para o POST
    public Cliente salvar(Cliente cliente) {
        // --- REGRA DE NEGÓCIO ---
        // 1. Verificar se o CPF já existe
        if (clienteRepository.findByCpf(cliente.getCpf()).isPresent()) {
            throw new RuntimeException("CPF já cadastrado.");
        }
        // 2. Verificar se o E-mail já existe
        if (clienteRepository.findByEmail(cliente.getEmail()).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado.");
        }
        // -------------------------

        return clienteRepository.save(cliente);
    }

    // Método para o PUT (Atualização completa)
    public Cliente atualizar(Long id, Cliente clienteAtualizado) {
        Cliente clienteExistente = clienteRepository.findById(id)
                                                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado com o ID: " + id));

        // Aqui, precisaríamos de uma lógica mais robusta para checar
        // se o novo CPF ou E-mail já não pertencem a *outro* cliente.
        // Por simplicidade, vamos apenas atualizar:

        clienteExistente.setNome(clienteAtualizado.getNome());
        clienteExistente.setCpf(clienteAtualizado.getCpf());
        clienteExistente.setEmail(clienteAtualizado.getEmail());
        clienteExistente.setIsVip(clienteAtualizado.getIsVip());

        return clienteRepository.save(clienteExistente);
    }

    // Método para o PATCH (Atualização parcial)
    public Cliente atualizarParcial(Long id, Cliente clienteParcial) {
        Cliente clienteExistente = clienteRepository.findById(id)
                                                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado com o ID: " + id));

        if (clienteParcial.getNome() != null) {
            clienteExistente.setNome(clienteParcial.getNome());
        }
        if (clienteParcial.getCpf() != null) {
            // (Idealmente, checar se o novo CPF já não existe em outro cliente)
            clienteExistente.setCpf(clienteParcial.getCpf());
        }
        if (clienteParcial.getEmail() != null) {
            // (Idealmente, checar se o novo E-mail já não existe em outro cliente)
            clienteExistente.setEmail(clienteParcial.getEmail());
        }
        if (clienteParcial.getIsVip() != null) {
            clienteExistente.setIsVip(clienteParcial.getIsVip());
        }

        return clienteRepository.save(clienteExistente);
    }

    // Método para o DELETE
    public void deletar(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente não encontrado com o ID: " + id);
        }
        clienteRepository.deleteById(id);
    }
}
