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
        String modelo,
        Integer km,
        String observacoes,
        LocalDateTime dataAbertura,
        List<ItemResponse> itens,
        BigDecimal valorTotal
) {}