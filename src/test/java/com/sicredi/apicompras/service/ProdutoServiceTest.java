package com.sicredi.apicompras.service;

import com.sicredi.apicompras.dto.ProdutoDto;
import com.sicredi.apicompras.exception.ProdutoNotFoundException;
import com.sicredi.apicompras.model.Produto;
import com.sicredi.apicompras.repository.produto.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoServiceTest {

    @InjectMocks
    private ProdutoService produtoService;

    @Mock
    private ProdutoRepository produtoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBuscarPorIdComSucesso() {
        Long produtoId = 1L;
        Produto mockProduto = new Produto(produtoId, "Produto Teste", BigDecimal.valueOf(50.0));

        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(mockProduto));

        Produto produto = produtoService.buscarPorId(produtoId);

        assertNotNull(produto);
        assertEquals(produtoId, produto.getId());
        assertEquals("Produto Teste", produto.getNome());
        verify(produtoRepository, times(1)).findById(produtoId);
    }

    @Test
    void testBuscarPorIdProdutoNaoEncontrado() {
        Long id = 1L;
        when(produtoRepository.findById(id)).thenReturn(Optional.empty());

        ProdutoNotFoundException exception = assertThrows(ProdutoNotFoundException.class, () -> {
            produtoService.buscarPorId(id);
        });

        assertEquals("Produto com id " + id + " não foi encontrado", exception.getMessage());
        verify(produtoRepository, times(1)).findById(id);
    }

    @Test
    void testCadastrarProdutoComSucesso() {
        ProdutoDto produtoDto = new ProdutoDto("Produto Novo", BigDecimal.valueOf(100.0));
        Produto mockProduto = Produto.builder()
                .nome(produtoDto.nome())
                .valorUnitario(produtoDto.valorUnitario())
                .build();

        when(produtoRepository.save(any(Produto.class))).thenReturn(mockProduto);

        Produto produtoCadastrado = produtoService.cadastrar(produtoDto);

        assertNotNull(produtoCadastrado);
        assertEquals("Produto Novo", produtoCadastrado.getNome());
        assertEquals(BigDecimal.valueOf(100.0), produtoCadastrado.getValorUnitario());
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

}
