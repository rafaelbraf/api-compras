package com.sicredi.apicompras.controller;

import com.sicredi.apicompras.dto.CompraDto;
import com.sicredi.apicompras.model.Compra;
import com.sicredi.apicompras.service.CompraService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CompraControllerTest {

    @InjectMocks
    private CompraController compraController;

    @Mock
    private CompraService compraService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(compraController).build();
    }

    @Test
    void testCadastrarCompra() throws Exception {
        Compra compra = Compra.builder()
                .build();

        when(compraService.cadastrar(any(CompraDto.class))).thenReturn(compra);

        mockMvc.perform(post("/compras")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"cpfComprador\":\"12345678909\", \"dataHora\":\"2024-10-15T10:00:00Z\"}"))
                .andExpect(status().isCreated());

        verify(compraService, times(1)).cadastrar(any(CompraDto.class));
    }

}
