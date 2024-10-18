package com.sicredi.apicompras.service;

import com.sicredi.apicompras.dto.CompraDto;
import com.sicredi.apicompras.dto.CompraFiltroDto;
import com.sicredi.apicompras.dto.ItemCompraDto;
import com.sicredi.apicompras.dto.RelatorioCompraDto;
import com.sicredi.apicompras.exception.LimiteQuantidadeExcedidaException;
import com.sicredi.apicompras.model.Compra;
import com.sicredi.apicompras.model.Produto;
import com.sicredi.apicompras.repository.compra.CompraRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Service
public class CompraService {

    private static final Logger logger = LoggerFactory.getLogger(CompraService.class);
    private final CompraRepository compraRepository;
    private final ProdutoService produtoService;

    public CompraService(CompraRepository compraRepository, ProdutoService produtoService) {
        this.compraRepository = compraRepository;
        this.produtoService = produtoService;
    }

    public Page<Compra> pesquisarComFiltro(CompraFiltroDto filtro, Pageable pageable) {
        logger.info("Buscando compras com filtro {}", filtro);
        return compraRepository.buscarComprasComFiltro(
                filtro.cpfComprador(),
                filtro.dataInicio(),
                filtro.dataFim(),
                filtro.nomeProduto(),
                pageable
        );
    }

    public Compra cadastrar(CompraDto compra) {
        logger.info("Cadastrando compra: {}", compra);
        Compra novaCompra = Compra.builder()
                .cpfComprador(compra.cpfComprador())
                .dataHora(OffsetDateTime.now())
                .build();

        for (ItemCompraDto itemCompra : compra.itens()) {
            if (isExcedeuQuantidadeMaximaCompraProduto(compra.cpfComprador(), itemCompra)) {
                logger.error("CPF {} já comprou 3 ou mais unidades do produto {}", compra.cpfComprador(), itemCompra.produtoId());
                throw new LimiteQuantidadeExcedidaException(compra.cpfComprador(), itemCompra.produtoId().toString());
            }

            Produto produto = produtoService.buscarPorId(itemCompra.produtoId());
            novaCompra.adicionarItens(produto, itemCompra.quantidade());
        }

        return compraRepository.save(novaCompra);
    }

    private boolean isExcedeuQuantidadeMaximaCompraProduto(String cpfComprador, ItemCompraDto itemCompra) {
        Integer totalUnidadesCompradas = compraRepository.findQuantidadeTotalByCpfAndProdutoId(cpfComprador, itemCompra.produtoId());
        totalUnidadesCompradas = (totalUnidadesCompradas == null) ? 0 : totalUnidadesCompradas;

        return totalUnidadesCompradas + itemCompra.quantidade() > 3;
    }

    public Page<RelatorioCompraDto> gerarRelatorioPorPeriodo(LocalDate dataInicio, LocalDate dataFim, Pageable pageable) {
        logger.info("Gerando relatório de compras de {} a {}", dataInicio, dataFim);
        return compraRepository.buscarRelatorioCompraPorProduto(dataInicio, dataFim, pageable);
    }

}
