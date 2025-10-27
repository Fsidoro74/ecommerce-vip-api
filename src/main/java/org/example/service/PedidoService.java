package org.example.service;

import org.example.dto.EnderecoViaCepDTO;
import org.example.model.Cliente;
import org.example.model.Pedido;
import org.example.model.Produto;
import org.example.repository.ClienteRepository;
import org.example.repository.PedidoRepository;
import org.example.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private RestTemplate restTemplate;


    // --- Métodos de Busca (iguais) ---
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    public List<Pedido> buscarPorClienteId(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }


    // --- MÉTODO 'CRIAR' ATUALIZADO ---
    public Pedido criar(Long clienteId, List<Long> produtoIds) {

        // 1. Buscar a entidade Cliente
        Cliente cliente = clienteRepository.findById(clienteId)
                                           .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + clienteId));

        // 2. Buscar as entidades Produto e calcular o valor total (subtotal)
        List<Produto> produtosDoPedido = new ArrayList<>();
        double subTotal = 0.0;

        for (Long produtoId : produtoIds) {
            Produto produto = produtoRepository.findById(produtoId)
                                               .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + produtoId));

            if (produto.getQuantidadeEstoque() <= 0) {
                throw new RuntimeException("Produto fora de estoque: " + produto.getNome());
            }

            produtosDoPedido.add(produto);
            subTotal += produto.getPreco();
        }

        // 3. ✨ NOVA LÓGICA: APLICAR DESCONTO VIP ✨
        // Verifica se o cliente é VIP (campo 'isVip' que criamos)
        if (cliente.getIsVip()) {
            double desconto = subTotal * 0.10; // Aplica 10% de desconto
            subTotal = subTotal - desconto;

            // Log simples para vermos no console do IntelliJ
            System.out.println("Cliente VIP! Aplicando 10% de desconto (R$ " + desconto + ")");
        }
        // ------------------------------------

        // 4. LÓGICA: CHAMAR API EXTERNA (ViaCEP)
        String cepCliente = cliente.getCep();
        String urlViaCEP = "https://viacep.com.br/ws/" + cepCliente + "/json/";
        double frete = 0.0;

        try {
            EnderecoViaCepDTO endereco = restTemplate.getForObject(urlViaCEP, EnderecoViaCepDTO.class);

            // 5. LÓGICA: CALCULAR FRETE (Fictício)
            if (endereco != null && endereco.uf() != null) {
                if (endereco.uf().equalsIgnoreCase("SP")) {
                    frete = 10.0; // Frete fixo para SP
                } else {
                    frete = 30.0; // Frete para outros estados
                }
            } else {
                frete = 50.0; // Frete padrão se o ViaCEP falhar
            }
        } catch (Exception e) {
            System.err.println("Erro ao consultar ViaCEP: " + e.getMessage());
            frete = 50.0; // Frete padrão em caso de erro
        }

        // 6. Montar o Objeto Pedido (com o valor total = subtotal c/ desconto + frete)
        Pedido novoPedido = new Pedido();
        novoPedido.setCliente(cliente);
        novoPedido.setProdutos(produtosDoPedido);
        novoPedido.setDataPedido(LocalDate.now());
        novoPedido.setValorTotal(subTotal + frete); // <<<--- SOMA O FRETE AO SUBTOTAL JÁ COM DESCONTO
        novoPedido.setStatus("PROCESSANDO");

        // 7. Salvar o novo pedido no banco
        return pedidoRepository.save(novoPedido);
    }

    // ... (O restante da classe, atualizarStatus e deletar, continua igual) ...

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