package com.sicredi.apicompras.dto;

import java.util.List;

public record CompraDto(
        String cpfComprador,
        List<ItemCompraDto> itens
) {}
