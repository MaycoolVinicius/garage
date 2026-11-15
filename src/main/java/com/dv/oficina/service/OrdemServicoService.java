package com.dv.oficina.service;

import com.dv.oficina.dto.OrdemServicoResumo;
import com.dv.oficina.dto.response.ItemResponse;
import com.dv.oficina.dto.request.OrdemServicoRequest;
import com.dv.oficina.dto.response.OrdemServicoResponse;
import com.dv.oficina.model.Cliente;
import com.dv.oficina.model.ItemServico;
import com.dv.oficina.model.OrdemServico;
import com.dv.oficina.repository.ClienteRepository;
import com.dv.oficina.repository.OrdemServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdemServicoService {

    private final ClienteRepository clienteRepository;
    private final OrdemServicoRepository ordemServicoRepository;

    @Transactional
    public OrdemServicoResponse criar(OrdemServicoRequest request) {
        Cliente cliente = clienteRepository.findByTelefone(request.telefone().trim())
                .orElseGet(() -> {
                    Cliente novo = new Cliente();
                    novo.setNome(request.nomeCliente().trim());
                    novo.setTelefone(request.telefone().trim());
                    return clienteRepository.save(novo);
                });

        OrdemServico os = new OrdemServico();
        os.setCliente(cliente);
        os.setPlaca(request.placa().trim().toUpperCase());

        request.itens().forEach(dto -> {
            ItemServico item = new ItemServico();
            item.setTipo(dto.tipo());
            item.setDescricao(dto.descricao().trim());
            item.setQuantidade(dto.quantidade());
            item.setValorUnitario(dto.valorUnitario());
            os.adicionarItem(item);
        });

        os.recalcularTotal();

        return paraResponse(ordemServicoRepository.save(os));
    }

    @Transactional(readOnly = true)
    public OrdemServicoResponse buscarPorId(Long id) {
        OrdemServico os = ordemServicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Ordem de serviço não encontrada: " + id));
        return paraResponse(os);
    }

    private OrdemServicoResponse paraResponse(OrdemServico os) {
        var itens = os.getItens().stream()
                .map(i -> new ItemResponse(
                        i.getTipo(), i.getDescricao(), i.getQuantidade(),
                        i.getValorUnitario(), i.getSubtotal()))
                .toList();

        return new OrdemServicoResponse(
                os.getId(),
                os.getCliente().getNome(),
                os.getCliente().getTelefone(),
                os.getPlaca(),
                os.getDataAbertura(),
                itens,
                os.getValorTotal());
    }
    @Transactional(readOnly = true)
    public List<OrdemServicoResumo> listar(String busca) {
        var pagina = PageRequest.of(0, 50);
        List<OrdemServico> resultado = (busca == null || busca.isBlank())
                ? ordemServicoRepository.findAllByOrderByDataAberturaDesc(pagina)
                : ordemServicoRepository.buscar(busca.trim(), pagina);

        return resultado.stream()
                .map(os -> new OrdemServicoResumo(
                        os.getId(),
                        os.getCliente().getNome(),
                        os.getPlaca(),
                        os.getDataAbertura(),
                        os.getValorTotal()))
                .toList();
    }
}