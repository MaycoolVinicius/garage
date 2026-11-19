package com.dv.oficina.model;



import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class OrdemServicoTest {

    @Test
    void somaOsSubtotaisDeTodosOsItens() {
        OrdemServico os = new OrdemServico();
        os.adicionarItem(item(TipoItem.SERVICO, 1, "80.00"));
        os.adicionarItem(item(TipoItem.PECA, 2, "35.50"));

        os.recalcularTotal();

        assertThat(os.getValorTotal()).isEqualByComparingTo("151.00");
    }

    @Test
    void naoPerdeCentavosComValoresQuePrejudicamODouble() {
        OrdemServico os = new OrdemServico();
        os.adicionarItem(item(TipoItem.PECA, 3, "0.10"));

        os.recalcularTotal();

        assertThat(os.getValorTotal()).isEqualByComparingTo("0.30");
    }

    @Test
    void multiplicaQuantidadePeloValorUnitario() {
        OrdemServico os = new OrdemServico();
        os.adicionarItem(item(TipoItem.PECA, 3, "33.33"));

        os.recalcularTotal();

        assertThat(os.getValorTotal()).isEqualByComparingTo("99.99");
    }

    @Test
    void osSemItensTemTotalZero() {
        OrdemServico os = new OrdemServico();

        os.recalcularTotal();

        assertThat(os.getValorTotal()).isEqualByComparingTo("0");
    }

    private ItemServico item(TipoItem tipo, int quantidade, String valor) {
        ItemServico item = new ItemServico();
        item.setTipo(tipo);
        item.setDescricao("teste");
        item.setQuantidade(quantidade);
        item.setValorUnitario(new BigDecimal(valor));
        return item;
    }
}