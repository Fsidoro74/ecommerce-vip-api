package org.example.service;

import org.example.model.Cliente;
import org.example.model.Pedido;
import org.example.model.Produto;
import org.example.repository.ClienteRepository;
import org.example.repository.PedidoRepository;
import org.example.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    // --- Injetando TODOS os repositórios que vamos precisar ---
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;


    // --- Métodos de Busca ---

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    // Método customizado que criamos no Repository
    public List<Pedido> buscarPorClienteId(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    // --- Método Principal: Criar um Pedido ---
    // Este método é diferente do 'salvar' comum.
    // Ele recebe os IDs, busca as entidades, faz a lógica de negócio
    // e então salva o novo pedido.

    public Pedido criar(Long clienteId, List<Long> produtoIds) {

        // 1. Buscar a entidade Cliente
        Cliente cliente = clienteRepository.findById(clienteId)
                                           .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + clienteId));

        // 2. Buscar as entidades Produto e calcular o valor total
        List<Produto> produtosDoPedido = new ArrayList<>();
        double valorTotalCalculado = 0.0;

        for (Long produtoId : produtoIds) {
            Produto produto = produtoRepository.findById(produtoId)
                                               .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + produtoId));

            // --- REGRA DE NEGÓCIO (Opcional, mas recomendado) ---
            // Verificar se há estoque
            if (produto.getQuantidadeEstoque() <= 0) {
                throw new RuntimeException("Produto fora de estoque: " + produto.getNome());
            }

            produtosDoPedido.add(produto);
            valorTotalCalculado += produto.getPreco();

            // --- REGRA DE NEGÓCIO (Opcional) ---
            // Dar baixa no estoque
            // produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - 1);
            // produtoRepository.save(produto); // Salva a baixa no estoque
        }

        // 3. Montar o Objeto Pedido
        Pedido novoPedido = new Pedido();
        novoPedido.setCliente(cliente);
        novoPedido.setProdutos(produtosDoPedido);
        novoPedido.setDataPedido(LocalDate.now()); // Pega a data atual
        novoPedido.setValorTotal(valorTotalCalculado);
        novoPedido.setStatus("PROCESSANDO"); // Define um status inicial

        // 4. Salvar o novo pedido no banco
        return pedidoRepository.save(novoPedido);
    }

    // --- Métodos de Atualização e Deleção ---

    // Para um Pedido, um 'PATCH' (atualização parcial)
    // normalmente serve para mudar o STATUS (ex: "ENVIADO", "ENTREGUE")
    public Pedido atualizarStatus(Long id, String novoStatus) {
        Pedido pedidoExistente = pedidoRepository.findById(id)
                                                 .orElseThrow(() -> new RuntimeException("Pedido não encontrado com o ID: " + id));

        pedidoExistente.setStatus(novoStatus);
        return pedidoRepository.save(pedidoExistente);
    }


    public void deletar(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new RuntimeException("Pedido não encontrado com o ID: " + id);
        }
        pedidoRepository.deleteById(id);
    }
}
