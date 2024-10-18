package com.sicredi.apicompras.dto;

import java.math.BigDecimal;

public class RelatorioCompraDto {

    private String nomeProduto;
    private BigDecimal valorUnitario;
    private Integer quantidadeCompras;
    private BigDecimal totalVendido;

    public RelatorioCompraDto() {
    }

    public RelatorioCompraDto(String nomeProduto, BigDecimal valorUnitario, Integer quantidadeCompras, BigDecimal totalVendido) {
        this.nomeProduto = nomeProduto;
        this.valorUnitario = valorUnitario;
        this.quantidadeCompras = quantidadeCompras;
        this.totalVendido = totalVendido;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public Integer getQuantidadeCompras() {
        return quantidadeCompras;
    }

    public void setQuantidadeCompras(Integer quantidadeCompras) {
        this.quantidadeCompras = quantidadeCompras;
    }

    public BigDecimal getTotalVendido() {
        return totalVendido;
    }

    public void setTotalVendido(BigDecimal totalVendido) {
        this.totalVendido = totalVendido;
    }
}