package com.dv.oficina.dto.response;

public record ConfiguracaoResponse(
        String nome,
        String razaoSocial,
        String cnpj,
        String endereco,
        String telefone,
        String email,
        String logo
) {}