package com.dv.oficina.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ConfiguracaoRequest(
        @NotBlank @Size(max = 100) String nome,
        @Size(max = 150) String razaoSocial,
        @Size(max = 18) String cnpj,
        @Size(max = 200) String endereco,
        @Size(max = 20) String telefone,
        @Email @Size(max = 120) String email
) {}