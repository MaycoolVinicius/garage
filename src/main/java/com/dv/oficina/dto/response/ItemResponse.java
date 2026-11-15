package com.dv.oficina.dto.response;

import com.dv.oficina.model.TipoItem;

import java.math.BigDecimal;

public record ItemResponse(
        TipoItem tipo,
        String descricao,
        Integer quantidade,
        BigDecimal valorUnitario,
        BigDecimal subtotal
) {}