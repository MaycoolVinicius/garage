package com.dv.oficina.dto.request;

import com.dv.oficina.dto.request.ItemRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record OrdemServicoRequest(
        @NotBlank @Size(max = 120) String nomeCliente,
        @NotBlank @Size(max = 20) String telefone,
        @NotBlank @Size(max = 10) String placa,
        @NotEmpty @Valid List<ItemRequest> itens
) {}