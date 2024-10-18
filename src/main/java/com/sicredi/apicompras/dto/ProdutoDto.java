package com.sicredi.apicompras.dto;

import java.math.BigDecimal;

public record ProdutoDto(
        String nome,
        BigDecimal valorUnitario
) {}
