package com.sicredi.apicompras.exception;

public class ProdutoNotFoundException extends RuntimeException {

    public ProdutoNotFoundException(Long id) {
        super(String.format("Produto com id %s não foi encontrado", id));
    }

}
