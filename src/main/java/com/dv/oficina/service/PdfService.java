package com.dv.oficina.service;

import com.dv.oficina.dto.response.ConfiguracaoResponse;
import com.dv.oficina.dto.response.ItemResponse;
import com.dv.oficina.dto.response.OrdemServicoResponse;
import com.dv.oficina.model.TipoItem;
import com.dv.oficina.util.PdfFormatos;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final TemplateEngine templateEngine;
    private final ConfiguracaoService configuracaoService;

    public byte[] gerarPdf(OrdemServicoResponse os) {
        ConfiguracaoResponse empresa = configuracaoService.buscar();

        List<ItemResponse> pecas = filtrar(os, TipoItem.PECA);
        List<ItemResponse> servicos = filtrar(os, TipoItem.SERVICO);

        Context context = new Context(Locale.of("pt", "BR"));
        context.setVariable("os", os);
        context.setVariable("empresa", empresa);
        context.setVariable("pecas", pecas);
        context.setVariable("servicos", servicos);
        context.setVariable("subtotalPecas", somar(pecas));
        context.setVariable("subtotalServicos", somar(servicos));
        context.setVariable("fmt", new PdfFormatos());

        String html = templateEngine.process("os-template", context);

        try (ByteArrayOutputStream saida = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(saida);
            builder.run();
            return saida.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException("Falha ao gerar o PDF da OS " + os.id(), e);
        }
    }

    private List<ItemResponse> filtrar(OrdemServicoResponse os, TipoItem tipo) {
        return os.itens().stream().filter(i -> i.tipo() == tipo).toList();
    }

    private BigDecimal somar(List<ItemResponse> itens) {
        return itens.stream()
                .map(ItemResponse::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}