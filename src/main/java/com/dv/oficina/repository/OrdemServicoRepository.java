package com.dv.oficina.repository;

import com.dv.oficina.model.OrdemServico;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {

    @EntityGraph(attributePaths = "cliente")
    List<OrdemServico> findAllByOrderByDataAberturaDesc(Pageable pageable);

    @EntityGraph(attributePaths = "cliente")
    @Query("""
        select os from OrdemServico os
        where upper(os.placa) like upper(concat('%', :busca, '%'))
           or upper(os.cliente.nome) like upper(concat('%', :busca, '%'))
           or (:digitos <> '' and os.cliente.telefone like concat('%', :digitos, '%'))
        order by os.dataAbertura desc
        """)
    List<OrdemServico> buscar(@Param("busca") String busca,
                              @Param("digitos") String digitos,
                              Pageable pageable);
}