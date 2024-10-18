package com.sicredi.apicompras.dto;

import java.time.LocalDate;

public record CompraFiltroDto(
        String cpfComprador,
        String nomeProduto,
        LocalDate dataInicio,
        LocalDate dataFim
) {}
