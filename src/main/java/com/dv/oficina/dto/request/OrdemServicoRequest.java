package com.dv.oficina.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.List;

public record OrdemServicoRequest(
        @NotBlank @Size(max = 120) String nomeCliente,
        @NotBlank @Size(max = 20) String telefone,
        @NotBlank @Size(max = 10) String placa,
        @Size(max = 80) String modelo,
        @PositiveOrZero @Max(9_999_999) Integer km,
        @Size(max = 1000) String observacoes,
        @NotEmpty @Valid List<ItemRequest> itens
) {}