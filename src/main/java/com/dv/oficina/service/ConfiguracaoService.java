package com.dv.oficina.service;

import com.dv.oficina.dto.request.ConfiguracaoRequest;
import com.dv.oficina.dto.response.ConfiguracaoResponse;
import com.dv.oficina.model.ConfiguracaoOficina;
import com.dv.oficina.repository.ConfiguracaoOficinaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class ConfiguracaoService {

    private static final long ID = 1L;
    private static final int LIMITE_LOGO_BYTES = 500 * 1024;

    private final ConfiguracaoOficinaRepository repository;

    @Transactional(readOnly = true)
    public ConfiguracaoResponse buscar() {
        return paraResponse(carregar());
    }

    @Transactional(readOnly = true)
    public ConfiguracaoOficina obter() {
        return carregar();
    }

    @Transactional
    public ConfiguracaoResponse atualizar(ConfiguracaoRequest request) {
        String cnpj = apenasDigitos(request.cnpj());
        if (!cnpj.isEmpty() && !cnpjValido(cnpj)) {
            throw erro("CNPJ inválido.");
        }

        ConfiguracaoOficina config = carregar();
        config.setNome(request.nome().trim());
        config.setRazaoSocial(textoOuNulo(request.razaoSocial()));
        config.setCnpj(cnpj.isEmpty() ? null : cnpj);
        config.setEndereco(textoOuNulo(request.endereco()));
        config.setTelefone(textoOuNulo(request.telefone()));
        config.setEmail(textoOuNulo(request.email()));
        return paraResponse(repository.save(config));
    }

    @Transactional
    public ConfiguracaoResponse salvarLogo(byte[] bytes) {
        if (bytes.length == 0) {
            throw erro("Arquivo vazio.");
        }
        if (bytes.length > LIMITE_LOGO_BYTES) {
            throw erro("O logo deve ter no máximo 500 KB.");
        }
        String tipo = detectarTipo(bytes);
        if (tipo == null) {
            throw erro("Envie uma imagem PNG ou JPEG.");
        }

        ConfiguracaoOficina config = carregar();
        config.setLogo(bytes);
        config.setLogoTipo(tipo);
        return paraResponse(repository.save(config));
    }

    @Transactional
    public ConfiguracaoResponse removerLogo() {
        ConfiguracaoOficina config = carregar();
        config.setLogo(null);
        config.setLogoTipo(null);
        return paraResponse(repository.save(config));
    }

    private ConfiguracaoOficina carregar() {
        return repository.findById(ID)
                .orElseThrow(() -> new IllegalStateException("Configuração da oficina não encontrada."));
    }

    private ConfiguracaoResponse paraResponse(ConfiguracaoOficina c) {
        String logo = (c.getLogo() == null)
                ? null
                : "data:" + c.getLogoTipo() + ";base64," + Base64.getEncoder().encodeToString(c.getLogo());

        return new ConfiguracaoResponse(
                c.getNome(), c.getRazaoSocial(), c.getCnpj(),
                c.getEndereco(), c.getTelefone(), c.getEmail(), logo);
    }

    /** Reconhece o tipo pelos primeiros bytes do arquivo, sem confiar no nome nem no Content-Type. */
    private String detectarTipo(byte[] b) {
        boolean png = b.length > 8
                && (b[0] & 0xFF) == 0x89 && b[1] == 'P' && b[2] == 'N' && b[3] == 'G'
                && b[4] == 0x0D && b[5] == 0x0A && b[6] == 0x1A && b[7] == 0x0A;
        if (png) {
            return "image/png";
        }
        boolean jpeg = b.length > 3
                && (b[0] & 0xFF) == 0xFF && (b[1] & 0xFF) == 0xD8 && (b[2] & 0xFF) == 0xFF;
        return jpeg ? "image/jpeg" : null;
    }

    private String apenasDigitos(String texto) {
        return texto == null ? "" : texto.replaceAll("\\D", "");
    }

    private boolean cnpjValido(String c) {
        if (c.length() != 14 || c.chars().distinct().count() == 1) {
            return false;
        }
        int[] pesos1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesos2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        return digito(c, pesos1) == c.charAt(12) - '0'
                && digito(c, pesos2) == c.charAt(13) - '0';
    }

    private int digito(String c, int[] pesos) {
        int soma = 0;
        for (int i = 0; i < pesos.length; i++) {
            soma += (c.charAt(i) - '0') * pesos[i];
        }
        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }

    private String textoOuNulo(String texto) {
        return (texto == null || texto.isBlank()) ? null : texto.trim();
    }

    private ResponseStatusException erro(String mensagem) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, mensagem);
    }
}