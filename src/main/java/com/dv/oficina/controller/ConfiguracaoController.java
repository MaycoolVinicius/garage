package com.dv.oficina.controller;

import com.dv.oficina.dto.request.ConfiguracaoRequest;
import com.dv.oficina.dto.response.ConfiguracaoResponse;
import com.dv.oficina.service.ConfiguracaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/configuracao")
@RequiredArgsConstructor
public class ConfiguracaoController {

    private final ConfiguracaoService configuracaoService;

    @GetMapping
    public ConfiguracaoResponse buscar() {
        return configuracaoService.buscar();
    }

    @PutMapping
    public ConfiguracaoResponse atualizar(@Valid @RequestBody ConfiguracaoRequest request) {
        return configuracaoService.atualizar(request);
    }

    @PutMapping(value = "/logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ConfiguracaoResponse enviarLogo(@RequestParam("arquivo") MultipartFile arquivo) throws IOException {
        return configuracaoService.salvarLogo(arquivo.getBytes());
    }

    @DeleteMapping("/logo")
    public ConfiguracaoResponse removerLogo() {
        return configuracaoService.removerLogo();
    }
}