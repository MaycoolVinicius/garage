package com.dv.oficina.controller;

import com.dv.oficina.dto.request.OrdemServicoRequest;
import com.dv.oficina.dto.response.OrdemServicoResponse;
import com.dv.oficina.service.OrdemServicoService;
import com.dv.oficina.service.PdfService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/os")
@RequiredArgsConstructor
public class OrdemServicoController {

    private final PdfService pdfService;
    private final OrdemServicoService ordemServicoService;

    @PostMapping
    public ResponseEntity<OrdemServicoResponse> criar(@Valid @RequestBody OrdemServicoRequest request) {
        OrdemServicoResponse criada = ordemServicoService.criar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criada.id()).toUri();
        return ResponseEntity.created(location).body(criada);
    }

    @GetMapping("/{id}")
    public OrdemServicoResponse buscar(@PathVariable Long id) {
        return ordemServicoService.buscarPorId(id);
    }

    @GetMapping("/teste")
    public ResponseEntity<byte[]> gerarPdfTeste() {
        byte[] pdf = pdfService.gerarPdfTeste();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=os-teste.pdf")
                .body(pdf);
    }
}