package com.dv.oficina.controller;

import com.dv.oficina.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrdemServicoController {

    private final PdfService pdfService;

    @GetMapping("/os/teste")
    public ResponseEntity<byte[]> gerarPdfTeste() {
        byte[] pdf = pdfService.gerarPdfTeste();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=os-teste.pdf")
                .body(pdf);
    }
}