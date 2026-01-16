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

        if (probabilidadeChurn < 0.4) {
            return "Manter comunicação ativa";
        } else if (probabilidadeChurn < 0.7) {
            return "Oferta de upgrade ou desconto";
        } else {
            return "Oferta personalizada";
        }

    }
}


