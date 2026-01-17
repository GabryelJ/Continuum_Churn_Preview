package com.hackathon.continuum.service;

import org.springframework.stereotype.Service;

import com.hackathon.continuum.entity.AnaliseChurn;
import com.hackathon.continuum.entity.ResultadoChurn;
import com.hackathon.continuum.entity.SugestaoRetencaoChurn;
import com.hackathon.continuum.repository.SugestaoRetencaoChurnRepository;

@Service
public class SugestaoRetencaoChurnService {

    private final SugestaoRetencaoChurnRepository repository;

    public SugestaoRetencaoChurnService(SugestaoRetencaoChurnRepository repository) {
        this.repository = repository;
    }

    public SugestaoRetencaoChurn gerarSugestao(
            AnaliseChurn analiseChurn,
            ResultadoChurn resultadoChurn,
            Double probabilidadeChurn
    ) {

        String acaoRetencao = definirAcao(probabilidadeChurn);

        SugestaoRetencaoChurn sugestao = new SugestaoRetencaoChurn();
        sugestao.setAnaliseChurn(analiseChurn);
        sugestao.setResultadoChurn(resultadoChurn);
        sugestao.setAcaoRetencao(acaoRetencao);

        return repository.save(sugestao);
    }

    private String definirAcao(Double probabilidadeChurn) {

        if (probabilidadeChurn == null) {
            return "Análise indisponível";
        }

        if (probabilidadeChurn < 0.20) {
            return "Manter relacionamento padrão";
        }

        if (probabilidadeChurn < 0.40) {
            return "Comunicação ativa e incentivo ao engajamento";
        }

        if (probabilidadeChurn < 0.60) {
            return "Oferta de benefício leve (ex: brinde ou aula extra)";
        }

        if (probabilidadeChurn < 0.80) {
            return "Oferta de desconto ou upgrade de plano";
        }

        return "Ação personalizada com contato direto do time de retenção";
    }
}


