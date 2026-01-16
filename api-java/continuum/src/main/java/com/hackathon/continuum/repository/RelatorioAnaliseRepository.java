package com.hackathon.continuum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hackathon.continuum.dto.RelatorioAnaliseDTO;
import com.hackathon.continuum.entity.SugestaoRetencaoChurn;
import org.springframework.stereotype.Repository;

@Repository
public interface RelatorioAnaliseRepository extends JpaRepository<SugestaoRetencaoChurn, Long>{

    @Query("""
        SELECT new com.hackathon.continuum.dto.RelatorioAnaliseDTO(
            a.nome,
            r.probabilidadeChurn,
            r.risco,
            s.acaoRetencao,
            CONCAT(
                r.primeiroMaisRelevante, ', ',
                r.segundoMaisRelevante, ', ',
                r.terceiroMaisRelevante
            )
        )
        FROM SugestaoRetencaoChurn s
        JOIN s.analiseChurn a
        JOIN s.resultadoChurn r
        ORDER BY r.probabilidadeChurn DESC
    """)
    List<RelatorioAnaliseDTO> buscarRelatorioAnalises();
}