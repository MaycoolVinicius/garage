package com.dv.oficina.dto.request;

import com.dv.oficina.model.TipoItem;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ItemRequest(
        @NotNull TipoItem tipo,
        @NotBlank @Size(max = 200) String descricao,
        @NotNull @Positive Integer quantidade,
        @NotNull @DecimalMin("0.00") BigDecimal valorUnitario
) {}