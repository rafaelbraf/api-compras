package com.sicredi.apicompras.repository.compra;

import com.sicredi.apicompras.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long>, CompraRepositoryCustom {

    @Query("SELECT SUM(ic.quantidade) FROM Compra c JOIN c.itens ic WHERE c.cpfComprador = :cpfComprador AND ic.produto.id = :produtoId")
    Integer findQuantidadeTotalByCpfAndProdutoId(@Param("cpfComprador") String cpfComprador, @Param("produtoId") Long produtoId);

}
