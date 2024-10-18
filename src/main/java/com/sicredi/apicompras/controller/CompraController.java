package com.sicredi.apicompras.controller;

import com.sicredi.apicompras.dto.CompraDto;
import com.sicredi.apicompras.dto.CompraFiltroDto;
import com.sicredi.apicompras.dto.RelatorioCompraDto;
import com.sicredi.apicompras.model.Compra;
import com.sicredi.apicompras.service.CompraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/compras")
@Tag(name = "Compras", description = "Operações das compras")
public class CompraController {

    private static final Logger logger = LoggerFactory.getLogger(CompraController.class);

    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @GetMapping("/search")
    @Operation(
            summary = "Pesquisar compras por filtro",
            description = "Endpoint para pesquisar compras pelo CPF do Comprador, Nome do Produto, Data de Início e Data de Fim com paginação"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Compras encontradas com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Page<Compra>> pesquisarCompras(
            @RequestParam(required = false) String cpfComprador,
            @RequestParam(required = false) String nomeProduto,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dataFim,
            Pageable pageable
    ) {
        logger.info("Pesquisando compras com filtros - CPF: {}, Nome do Produto: {}, Data de Inicio: {}, Data de Fim: {}",
                cpfComprador, nomeProduto, dataInicio, dataFim);

        CompraFiltroDto filtro = new CompraFiltroDto(cpfComprador, nomeProduto, dataInicio, dataFim);
        Page<Compra> comprasEncontradas = compraService.pesquisarComFiltro(filtro, pageable);

        return ResponseEntity.ok(comprasEncontradas);
    }

    @GetMapping("/relatorio")
    @Operation(
            summary = "Gerar relatório de compras",
            description = "Endpoint para gerar um relatório de compras no período especificado com paginação"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos ou fora do formato"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Page<RelatorioCompraDto>> obterRelatorio(
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dataInicio,
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dataFim,
            Pageable pageable
    ) {
        logger.info("Buscando relatorio de compras de {} a {}", dataInicio, dataFim);
        Page<RelatorioCompraDto> relatorio = compraService.gerarRelatorioPorPeriodo(dataInicio, dataFim, pageable);
        return ResponseEntity.ok(relatorio);
    }

    @PostMapping
    @Operation(
            summary = "Cadastrar uma nova compra",
            description = "Endpoint para cadastrar uma nova compra"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Compra cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos para o cadastro"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado para cadastro da compra"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<Compra> cadastrarCompra(@RequestBody CompraDto compra) {
        logger.info("Iniciando cadastro de compra: {}", compra);
        Compra compraCadastrada = compraService.cadastrar(compra);
        return ResponseEntity.status(HttpStatus.CREATED).body(compraCadastrada);
    }

}
