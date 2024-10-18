package com.sicredi.apicompras.model;

public record ErrorResponse(
        String mensagem,
        int status
) {}
