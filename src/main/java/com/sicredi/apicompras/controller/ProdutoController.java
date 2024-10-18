package com.sicredi.apicompras.controller;

import com.sicredi.apicompras.dto.ProdutoDto;
import com.sicredi.apicompras.model.Produto;
import com.sicredi.apicompras.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/produtos")
@Tag(name = "Produtos", description = "Operações dos Produtos")
public class ProdutoController {

    private static final Logger logger = LoggerFactory.getLogger(ProdutoController.class);

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    @Operation(
            summary = "Buscar todos os produtos",
            description = "Endpoint para buscar todos os produtos com paginação"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produtos encontrados")
    })
    public ResponseEntity<Page<Produto>> buscarProdutos(Pageable pageable) {
        logger.info("Buscando todos os produtos...");
        Page<Produto> produtosEncontrados = produtoService.buscarTodos(pageable);
        return ResponseEntity.ok(produtosEncontrados);
    }

    @PostMapping
    @Operation(
            summary = "Cadastrar um novo produto",
            description = "Endpoint para cadastrar um novo produto."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos para cadastro"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Produto> cadastrar(@RequestBody ProdutoDto produto) {
        logger.info("Iniciando cadastro de produto: {}", produto);
        Produto produtoCadastrado = produtoService.cadastrar(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoCadastrado);
    }

}
