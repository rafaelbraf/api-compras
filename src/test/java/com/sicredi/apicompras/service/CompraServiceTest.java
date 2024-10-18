package com.sicredi.apicompras.service;

import com.sicredi.apicompras.dto.CompraDto;
import com.sicredi.apicompras.dto.CompraFiltroDto;
import com.sicredi.apicompras.dto.ItemCompraDto;
import com.sicredi.apicompras.dto.RelatorioCompraDto;
import com.sicredi.apicompras.exception.LimiteQuantidadeExcedidaException;
import com.sicredi.apicompras.model.Compra;
import com.sicredi.apicompras.model.Produto;
import com.sicredi.apicompras.repository.compra.CompraRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.data.domain.PageRequest.of;

class CompraServiceTest {

    @InjectMocks
    private CompraService compraService;

    @Mock
    private CompraRepository compraRepository;

    @Mock
    private ProdutoService produtoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCadastrarCompraComSucesso() {
        CompraDto compra = new CompraDto("12345678910", List.of(new ItemCompraDto(1L, 2)));
        Produto mockProduto = new Produto(1L, "Produto Teste", BigDecimal.TEN);

        when(produtoService.buscarPorId(1L)).thenReturn(mockProduto);
        when(compraRepository.save(any(Compra.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(compraRepository.findQuantidadeTotalByCpfAndProdutoId("12345678900", 1L)).thenReturn(1);

        Compra novaCompra = compraService.cadastrar(compra);

        assertNotNull(novaCompra);
        assertEquals("12345678910", novaCompra.getCpfComprador());
        assertEquals(1, novaCompra.getItens().size());
        assertEquals(2, novaCompra.getItens().getFirst().getQuantidade());
        verify(compraRepository, times(1)).save(any(Compra.class));
    }

    @Test
    void testCadastrarCompraLimiteExcedido() {
        String cpfComprador = "12345678909";
        Long produtoId = 1L;

        when(compraRepository.findQuantidadeTotalByCpfAndProdutoId(anyString(), anyLong())).thenReturn(3);

        var compraDto = new CompraDto(cpfComprador, List.of(new ItemCompraDto(produtoId, 1)));

        assertThrows(LimiteQuantidadeExcedidaException.class, () -> compraService.cadastrar(compraDto));
    }

    @Test
    void testPesquisarComFiltro() {
        CompraFiltroDto filtro = new CompraFiltroDto("12345678910", "Produto Teste", LocalDate.now(), LocalDate.now());
        Pageable pageable = of(0, 10);

        Compra compra = new Compra();
        Page<Compra> page = new PageImpl<>(Collections.singletonList(compra), pageable, 1);

        when(compraRepository.buscarComprasComFiltro(any(), any(), any(), any(), any(Pageable.class))).thenReturn(page);

        Page<Compra> result = compraService.pesquisarComFiltro(filtro, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(compra, result.getContent().getFirst());

        verify(compraRepository, times(1)).buscarComprasComFiltro(
                filtro.cpfComprador(),
                filtro.dataInicio(),
                filtro.dataFim(),
                filtro.nomeProduto(),
                pageable
        );
    }

    @Test
    void testGerarRelatorioPorPeriodo() {
        LocalDate dataInicio = LocalDate.now().minusDays(10);
        LocalDate dataFim = LocalDate.now();
        Pageable pageable = PageRequest.of(0, 10);
        List<RelatorioCompraDto> mockRelatorio = List.of(
                new RelatorioCompraDto("Produto Teste", BigDecimal.TEN, 5, BigDecimal.valueOf(50.0))
        );
        Page<RelatorioCompraDto> mockPage = new PageImpl<>(mockRelatorio, pageable, 1);

        when(compraRepository.buscarRelatorioCompraPorProduto(dataInicio, dataFim, pageable)).thenReturn(mockPage);

        Page<RelatorioCompraDto> relatorioPage = compraService.gerarRelatorioPorPeriodo(dataInicio, dataFim, pageable);

        assertNotNull(relatorioPage);
        assertEquals(1, relatorioPage.getTotalElements());
        assertEquals(1, relatorioPage.getContent().size());
        verify(compraRepository, times(1)).buscarRelatorioCompraPorProduto(dataInicio, dataFim, pageable);
    }

}
