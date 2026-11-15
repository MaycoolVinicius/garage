package com.dv.oficina.dto.response;

import com.dv.oficina.dto.response.ItemResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrdemServicoResponse(
        Long id,
        String nomeCliente,
        String telefone,
        String placa,
        LocalDateTime dataAbertura,
        List<ItemResponse> itens,
        BigDecimal valorTotal
) {}