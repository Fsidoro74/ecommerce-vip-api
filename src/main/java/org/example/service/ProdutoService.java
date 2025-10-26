package org.example.service;

import org.example.model.Produto;
import org.example.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Indica ao Spring que esta é uma classe de serviço
public class ProdutoService {

    // Injeção de dependência: O Spring vai "injetar" (fornecer)
    // uma instância do ProdutoRepository para nós usarmos.
    @Autowired
    private ProdutoRepository produtoRepository;

    // Método para o GET (todos)
    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    // Método para o GET (por ID)
    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }

    // Método para o POST
    public Produto salvar(Produto produto) {
        // REGRA DE NEGÓCIO: Não salvar se o SKU já existir.
        if (produtoRepository.findBySku(produto.getSku()).isPresent()) {
            // Lança uma exceção se o SKU já estiver em uso.
            throw new RuntimeException("SKU já cadastrado. Por favor, utilize outro.");
        }
        return produtoRepository.save(produto);
    }

    // Método para o PUT (Atualização completa)
    public Produto atualizar(Long id, Produto produtoAtualizado) {
        // 1. Busca o produto no banco
        Produto produtoExistente = produtoRepository.findById(id)
                                                    .orElseThrow(() -> new RuntimeException("Produto não encontrado com o ID: " + id));

        // 2. Atualiza todos os campos
        produtoExistente.setNome(produtoAtualizado.getNome());
        produtoExistente.setDescricao(produtoAtualizado.getDescricao());
        produtoExistente.setSku(produtoAtualizado.getSku());
        produtoExistente.setPreco(produtoAtualizado.getPreco());
        produtoExistente.setQuantidadeEstoque(produtoAtualizado.getQuantidadeEstoque());

        // 3. Salva (o JPA entende que é uma atualização por causa do ID)
        return produtoRepository.save(produtoExistente);
    }

    // Método para o PATCH (Atualização parcial)
    public Produto atualizarParcial(Long id, Produto produtoParcial) {
        // 1. Busca o produto no banco
        Produto produtoExistente = produtoRepository.findById(id)
                                                    .orElseThrow(() -> new RuntimeException("Produto não encontrado com o ID: " + id));

        // 2. Verifica campo a campo se veio alguma atualização
        //    (Graças aos Wrappers, podemos checar se é 'null')
        if (produtoParcial.getNome() != null) {
            produtoExistente.setNome(produtoParcial.getNome());
        }
        if (produtoParcial.getDescricao() != null) {
            produtoExistente.setDescricao(produtoParcial.getDescricao());
        }
        if (produtoParcial.getSku() != null) {
            produtoExistente.setSku(produtoParcial.getSku());
        }
        if (produtoParcial.getPreco() != null) {
            produtoExistente.setPreco(produtoParcial.getPreco());
        }
        if (produtoParcial.getQuantidadeEstoque() != null) {
            produtoExistente.setQuantidadeEstoque(produtoParcial.getQuantidadeEstoque());
        }

        // 3. Salva o produto atualizado
        return produtoRepository.save(produtoExistente);
    }


    // Método para o DELETE
    public void deletar(Long id) {
        // 1. Verifica se o produto existe antes de tentar deletar
        if (!produtoRepository.existsById(id)) {
            throw new RuntimeException("Produto não encontrado com o ID: " + id);
        }
        // 2. Deleta
        produtoRepository.deleteById(id);
    }
}
