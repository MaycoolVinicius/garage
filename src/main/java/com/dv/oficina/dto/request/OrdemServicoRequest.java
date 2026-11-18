package com.dv.oficina.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record OrdemServicoRequest(
        @NotBlank @Size(max = 120) String nomeCliente,
        @NotBlank @Size(max = 20)
        @Pattern(regexp = "^(?:\\D*\\d){10,11}\\D*$", message = "telefone deve ter 10 ou 11 dígitos")
        String telefone,
        @NotBlank @Size(max = 10) String placa,
        @Size(max = 80) String modelo,
        @PositiveOrZero @Max(9_999_999) Integer km,
        @Size(max = 1000) String observacoes,
        @NotEmpty @Valid List<ItemRequest> itens
) {}