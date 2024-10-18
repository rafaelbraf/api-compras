package com.sicredi.apicompras.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "compras")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cpf_comprador", nullable = false)
    private String cpfComprador;

    @Column(name = "data_hora", nullable = false)
    private OffsetDateTime dataHora;

    @Builder.Default
    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ItemCompra> itens = new ArrayList<>();

    public void adicionarItens(Produto produto, Integer quantidade) {
        ItemCompra item = ItemCompra.builder()
                .produto(produto)
                .quantidade(quantidade)
                .compra(this)
                .build();

        itens.add(item);
    }

    public void removerItem(ItemCompra item) {
        itens.remove(item);
        item.setCompra(null);
    }

}
