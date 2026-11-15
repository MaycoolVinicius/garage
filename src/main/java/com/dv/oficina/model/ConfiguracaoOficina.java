package com.dv.oficina.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "configuracao_oficina")
@Getter
@Setter
@NoArgsConstructor
public class ConfiguracaoOficina {

    @Id
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(name = "razao_social", length = 150)
    private String razaoSocial;

    @Column(length = 14)
    private String cnpj;

    @Column(length = 200)
    private String endereco;

    @Column(length = 20)
    private String telefone;

    @Column(length = 120)
    private String email;

    private byte[] logo;

    @Column(name = "logo_tipo", length = 20)
    private String logoTipo;
}