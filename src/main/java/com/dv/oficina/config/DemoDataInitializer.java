package com.dv.oficina.config;

import com.dv.oficina.dto.request.ConfiguracaoRequest;
import com.dv.oficina.dto.request.ItemRequest;
import com.dv.oficina.dto.request.OrdemServicoRequest;
import com.dv.oficina.model.TipoItem;
import com.dv.oficina.repository.OrdemServicoRepository;
import com.dv.oficina.service.ConfiguracaoService;
import com.dv.oficina.service.OrdemServicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

@Component
@Profile("demo")
@RequiredArgsConstructor
public class DemoDataInitializer implements ApplicationRunner {

    private final OrdemServicoRepository ordemServicoRepository;
    private final OrdemServicoService ordemServicoService;
    private final ConfiguracaoService configuracaoService;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!datasourceUrl.toLowerCase().contains("demo")) {
            throw new IllegalStateException(
                    "Perfil demo recusado: a URL do banco não contém 'demo'. "
                            + "Use um banco de demonstração (ex.: oficina_demo) para evitar sobrescrever dados reais.");
        }

        if (ordemServicoRepository.count() > 0) {
            return;
        }

        configuracaoService.atualizar(new ConfiguracaoRequest(
                "Garage",
                "Garage Centro Automotivo Ltda",
                "11222333000181",
                "Rua Exemplo, 123 - Centro - Curitiba/PR",
                "(41) 3000-0000",
                "contato@garage.example"));

        try (InputStream logo = new ClassPathResource("demo/logo.png").getInputStream()) {
            configuracaoService.salvarLogo(logo.readAllBytes());
        }

        ordemServicoService.criar(new OrdemServicoRequest(
                "Ana Exemplo", "11900000001", "DEM1A01", "Hatch 1.0", 48200,
                "Cliente relatou vibração no volante acima de 80 km/h.",
                List.of(
                        item(TipoItem.SERVICO, "Alinhamento 3D", 1, "90.00"),
                        item(TipoItem.SERVICO, "Balanceamento (4 rodas)", 4, "25.00"))));

        ordemServicoService.criar(new OrdemServicoRequest(
                "Bruno Teste", "11900000002", "DEM2B02", "Sedan 2.0", 91500,
                null,
                List.of(
                        item(TipoItem.PECA, "Pneu aro 15", 2, "389.90"),
                        item(TipoItem.PECA, "Válvula de pneu", 2, "7.50"),
                        item(TipoItem.SERVICO, "Montagem e balanceamento", 2, "35.00"))));

        ordemServicoService.criar(new OrdemServicoRequest(
                "Carla Modelo", "11900000003", "DEM3C03", "SUV 1.6", 23750,
                "Revisão de suspensão dianteira.",
                List.of(
                        item(TipoItem.PECA, "Pivô de suspensão", 2, "129.90"),
                        item(TipoItem.PECA, "Bucha da bandeja", 2, "48.75"),
                        item(TipoItem.SERVICO, "Mão de obra - suspensão", 1, "180.00"))));
    }

    private ItemRequest item(TipoItem tipo, String descricao, int quantidade, String valor) {
        return new ItemRequest(tipo, descricao, quantidade, new BigDecimal(valor));
    }


}