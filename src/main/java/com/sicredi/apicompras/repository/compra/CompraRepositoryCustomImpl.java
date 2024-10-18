package com.sicredi.apicompras.repository.compra;

import com.sicredi.apicompras.dto.RelatorioCompraDto;
import com.sicredi.apicompras.model.Compra;
import com.sicredi.apicompras.model.ItemCompra;
import com.sicredi.apicompras.model.Produto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

public class CompraRepositoryCustomImpl implements CompraRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Compra> buscarComprasComFiltro(String cpfComprador, LocalDate dataInicio, LocalDate dataFim, String nomeProduto, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Compra> query = cb.createQuery(Compra.class);
        Root<Compra> compra = query.from(Compra.class);
        Join<Compra, ItemCompra> itensJoin = compra.join("itens", JoinType.LEFT);
        Join<ItemCompra, Produto> produtosJoin = itensJoin.join("produto", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if (nonNull(cpfComprador) && !cpfComprador.isEmpty()) {
            predicates.add(cb.equal(compra.get("cpfComprador"), cpfComprador));
        }

        if (nonNull(dataInicio) && nonNull(dataFim)) {
            predicates.add(cb.between(
                    compra.get("dataHora").as(LocalDate.class),
                    dataInicio,
                    dataFim
            ));
        }

        if (nonNull(nomeProduto) && !nomeProduto.isEmpty()) {
            predicates.add(cb.like(cb.lower(produtosJoin.get("nome")), "%" + nomeProduto.toLowerCase() + "%"));
        }

        query.select(compra).distinct(true)
                .where(cb.and(predicates.toArray(new Predicate[0])));

        TypedQuery<Compra> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Compra> resultado = typedQuery.getResultList();
        int totalRows = resultado.size();

        return new PageImpl<>(resultado, pageable, totalRows);
    }

    @Override
    public Page<RelatorioCompraDto> buscarRelatorioCompraPorProduto(LocalDate dataInicio, LocalDate dataFim, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<RelatorioCompraDto> query = cb.createQuery(RelatorioCompraDto.class);

        Root<Compra> compraRoot = query.from(Compra.class);
        Join<Compra, ItemCompra> itensJoin = compraRoot.join("itens", JoinType.INNER);
        Join<ItemCompra, Produto> produtosJoin = itensJoin.join("produto", JoinType.INNER);

        query.select(cb.construct(
                RelatorioCompraDto.class,
                produtosJoin.get("nome"),
                produtosJoin.get("valorUnitario"),
                cb.sum(itensJoin.get("quantidade")),
                cb.sum(cb.prod(itensJoin.get("quantidade"), produtosJoin.get("valorUnitario")))
        ));

        query.where(cb.between(
                compraRoot.get("dataHora").as(LocalDate.class),
                dataInicio,
                dataFim
        ));

        query.groupBy(produtosJoin.get("nome"), produtosJoin.get("valorUnitario"));

        TypedQuery<RelatorioCompraDto> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<RelatorioCompraDto> resultado = typedQuery.getResultList();
        int totalRows = resultado.size();

        return new PageImpl<>(resultado, pageable, totalRows);
    }

}
