package com.dv.oficina.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ordem_servico")
@Getter
@Setter
@NoArgsConstructor
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Column(nullable = false, length = 10)
    private String placa;

    @Column(length = 80)
    private String modelo;

    private Integer km;

    @Column(length = 1000)
    private String observacoes;

    @Column(nullable = false)
    private LocalDateTime dataAbertura = LocalDateTime.now();

    @OneToMany(mappedBy = "ordemServico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemServico> itens = new ArrayList<>();

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valorTotal = BigDecimal.ZERO;

    public void adicionarItem(ItemServico item) {
        item.setOrdemServico(this);
        itens.add(item);
    }

    public void recalcularTotal() {
        this.valorTotal = itens.stream()
                .map(ItemServico::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}