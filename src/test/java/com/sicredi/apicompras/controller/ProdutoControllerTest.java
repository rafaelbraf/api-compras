package com.sicredi.apicompras.controller;

import com.sicredi.apicompras.dto.ProdutoDto;
import com.sicredi.apicompras.model.Produto;
import com.sicredi.apicompras.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProdutoControllerTest {

    @InjectMocks
    private ProdutoController produtoController;

    @Mock
    private ProdutoService produtoService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(produtoController).build();
    }

    @Test
    void testCadastrarProduto() throws Exception {
        Produto produto = Produto.builder()
                .nome("Água")
                .valorUnitario(BigDecimal.TWO)
                .build();

        when(produtoService.cadastrar(any(ProdutoDto.class))).thenReturn(produto);

        mockMvc.perform(post("/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Água\", \"valorUnitario\":2}"))
                .andExpect(status().isCreated());

        verify(produtoService, times(1)).cadastrar(any(ProdutoDto.class));
    }

}
