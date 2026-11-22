package com.dv.oficina.service;

import com.dv.oficina.dto.request.ItemRequest;
import com.dv.oficina.dto.request.OrdemServicoRequest;
import com.dv.oficina.dto.response.OrdemServicoResponse;
import com.dv.oficina.model.Cliente;
import com.dv.oficina.model.OrdemServico;
import com.dv.oficina.model.TipoItem;
import com.dv.oficina.repository.ClienteRepository;
import com.dv.oficina.repository.OrdemServicoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrdemServicoServiceTest {

    @Mock ClienteRepository clienteRepository;
    @Mock OrdemServicoRepository ordemServicoRepository;
    @InjectMocks OrdemServicoService service;

    @Test
    void buscaOClientePeloTelefoneApenasComDigitos() {
        when(clienteRepository.findByTelefone("41999990000"))
                .thenReturn(Optional.of(cliente("Maria", "41999990000")));
        salvarOsRetornandoOMesmoObjeto();

        service.criar(request("Maria", "(41) 99999-0000", "abc1d23", null, null, itemSimples()));

        verify(clienteRepository).findByTelefone("41999990000");
    }

    @Test
    void reaproveitaClienteExistenteEmVezDeCriarOutro() {
        when(clienteRepository.findByTelefone("41999990000"))
                .thenReturn(Optional.of(cliente("Maria", "41999990000")));
        salvarOsRetornandoOMesmoObjeto();

        OrdemServicoResponse resposta = service.criar(
                request("Nome Diferente", "41999990000", "abc1d23", null, null, itemSimples()));

        verify(clienteRepository, never()).save(any());
        assertThat(resposta.nomeCliente()).isEqualTo("Maria");
    }

    @Test
    void criaClienteNovoComTelefoneSoComDigitosENomeSemEspacos() {
        when(clienteRepository.findByTelefone("41999990000")).thenReturn(Optional.empty());
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(inv -> inv.getArgument(0));
        salvarOsRetornandoOMesmoObjeto();

        service.criar(request("  Joao  ", "(41) 99999-0000", "abc1d23", null, null, itemSimples()));

        ArgumentCaptor<Cliente> captor = ArgumentCaptor.forClass(Cliente.class);
        verify(clienteRepository).save(captor.capture());
        assertThat(captor.getValue().getNome()).isEqualTo("Joao");
        assertThat(captor.getValue().getTelefone()).isEqualTo("41999990000");
    }

    @Test
    void gravaAPlacaEmMaiusculasESemEspacos() {
        when(clienteRepository.findByTelefone("41999990000"))
                .thenReturn(Optional.of(cliente("Maria", "41999990000")));
        salvarOsRetornandoOMesmoObjeto();

        OrdemServicoResponse resposta = service.criar(
                request("Maria", "41999990000", "  abc1d23 ", null, null, itemSimples()));

        assertThat(resposta.placa()).isEqualTo("ABC1D23");
    }

    @Test
    void modeloEObservacoesEmBrancoViramNulo() {
        when(clienteRepository.findByTelefone("41999990000"))
                .thenReturn(Optional.of(cliente("Maria", "41999990000")));
        salvarOsRetornandoOMesmoObjeto();

        service.criar(request("Maria", "41999990000", "abc1d23", "   ", "", itemSimples()));

        ArgumentCaptor<OrdemServico> captor = ArgumentCaptor.forClass(OrdemServico.class);
        verify(ordemServicoRepository).save(captor.capture());
        assertThat(captor.getValue().getModelo()).isNull();
        assertThat(captor.getValue().getObservacoes()).isNull();
    }

    @Test
    void totalEhCalculadoNoServidorASomarOsItens() {
        when(clienteRepository.findByTelefone("41999990000"))
                .thenReturn(Optional.of(cliente("Maria", "41999990000")));
        salvarOsRetornandoOMesmoObjeto();

        OrdemServicoResponse resposta = service.criar(request(
                "Maria", "41999990000", "abc1d23", null, null,
                new ItemRequest(TipoItem.SERVICO, "Alinhamento", 1, new BigDecimal("80.00")),
                new ItemRequest(TipoItem.PECA, "Pastilha", 2, new BigDecimal("35.50"))));

        assertThat(resposta.valorTotal()).isEqualByComparingTo("151.00");
    }

    private void salvarOsRetornandoOMesmoObjeto() {
        when(ordemServicoRepository.save(any(OrdemServico.class)))
                .thenAnswer(inv -> inv.getArgument(0));
    }

    private Cliente cliente(String nome, String telefone) {
        Cliente c = new Cliente();
        c.setNome(nome);
        c.setTelefone(telefone);
        return c;
    }

    private ItemRequest itemSimples() {
        return new ItemRequest(TipoItem.SERVICO, "Alinhamento", 1, new BigDecimal("100.00"));
    }

    private OrdemServicoRequest request(String nome, String telefone, String placa,
                                        String modelo, String observacoes, ItemRequest... itens) {
        return new OrdemServicoRequest(nome, telefone, placa, modelo, null, observacoes, List.of(itens));
    }
}