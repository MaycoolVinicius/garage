CREATE TABLE configuracao_oficina (
    id            BIGINT       PRIMARY KEY CHECK (id = 1),
    nome          VARCHAR(100) NOT NULL,
    razao_social  VARCHAR(150),
    cnpj          VARCHAR(14),
    endereco      VARCHAR(200),
    telefone      VARCHAR(20),
    email         VARCHAR(120),
    logo          BYTEA,
    logo_tipo     VARCHAR(20)
);

INSERT INTO configuracao_oficina (id, nome) VALUES (1, 'Minha Oficina');