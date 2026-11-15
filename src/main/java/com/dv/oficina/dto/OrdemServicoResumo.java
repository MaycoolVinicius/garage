package com.dv.oficina.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrdemServicoResumo(
        Long id,
        String nomeCliente,
        String placa,
        LocalDateTime dataAbertura,
        BigDecimal valorTotal
) {}