package com.sicredi.apicompras.exception;

public class LimiteQuantidadeExcedidaException extends RuntimeException {

    public LimiteQuantidadeExcedidaException(String cpf, String produtoId) {
        super(String.format("O CPF %s já comprou 3 ou mais unidades do produto %s", cpf, produtoId));
    }

}
