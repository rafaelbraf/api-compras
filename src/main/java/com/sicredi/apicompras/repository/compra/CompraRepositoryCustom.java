package com.sicredi.apicompras.repository.compra;

import com.sicredi.apicompras.dto.RelatorioCompraDto;
import com.sicredi.apicompras.model.Compra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface CompraRepositoryCustom {

    Page<Compra> buscarComprasComFiltro(
            String cpfComprador,
            LocalDate dataInicio,
            LocalDate dataFim,
            String nomeProduto,
            Pageable pageable
    );

    Page<RelatorioCompraDto> buscarRelatorioCompraPorProduto(LocalDate dataInicio, LocalDate dataFim, Pageable pageable);

}
