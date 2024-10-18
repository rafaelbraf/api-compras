package com.sicredi.apicompras.service;

import com.sicredi.apicompras.dto.ProdutoDto;
import com.sicredi.apicompras.exception.ProdutoNotFoundException;
import com.sicredi.apicompras.model.Produto;
import com.sicredi.apicompras.repository.produto.ProdutoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {

    private static final Logger logger = LoggerFactory.getLogger(ProdutoService.class);

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Page<Produto> buscarTodos(Pageable pageable) {
        return produtoRepository.findAll(pageable);
    }

    public Produto buscarPorId(Long id) {
        logger.info("Buscando produto com id {}...", id);
        return produtoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Produto com id {} não encontrado", id);
                    return new ProdutoNotFoundException(id);
                });
    }

    public Produto cadastrar(ProdutoDto produto) {
        logger.info("Cadastrando novo produto {}", produto);
        Produto novoProduto = Produto.builder()
                .nome(produto.nome())
                .valorUnitario(produto.valorUnitario())
                .build();

        return produtoRepository.save(novoProduto);
    }
}
