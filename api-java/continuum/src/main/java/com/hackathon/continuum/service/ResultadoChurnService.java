package com.hackathon.continuum.service;

import org.springframework.stereotype.Service;

import com.hackathon.continuum.dto.RespostaDTO;
import com.hackathon.continuum.entity.AnaliseChurn;
import com.hackathon.continuum.entity.ResultadoChurn;
import com.hackathon.continuum.repository.ResultadoChurnRepository;

@Service
public class ResultadoChurnService {

    private final ResultadoChurnRepository repository;

    public ResultadoChurnService(ResultadoChurnRepository repository) {
        this.repository = repository;
    }

    public ResultadoChurn salvarResultado(
            AnaliseChurn analiseChurn,
            RespostaDTO respostaDTO
    ) {

        ResultadoChurn resultado = new ResultadoChurn();

        resultado.setAnaliseChurn(analiseChurn);
        resultado.setProbabilidadeChurn(respostaDTO.probabilidade_churn());
        resultado.setRisco(respostaDTO.risco());
        resultado.setPrimeiroMaisRelevante(respostaDTO.primeiro_mais_relevante());
        resultado.setSegundoMaisRelevante(respostaDTO.segundo_mais_relevante());
        resultado.setTerceiroMaisRelevante(respostaDTO.terceiro_mais_relevante());

        return repository.save(resultado);
    }

    public long obterTotalRegistros() { return repository.count(); }

    public long obterTotalRegistrosComRisco(String risco){
        return repository.countByRisco(risco.toUpperCase());
    }

    public double obterTaxaDeClientesComRiscoAltoEmPorcentagem(){
        long totalRegistros = obterTotalRegistros();
        long totalRegistrosRiscoAlto = obterTotalRegistrosComRisco("alto");

        if (totalRegistros == 0) {
            return 0.0;
        }

        return (double) totalRegistrosRiscoAlto / totalRegistros * 100;
    }

    public double obterMediaDasTaxasDeRiscoEmPorcentagem(){
        long totalRegistros = obterTotalRegistros();
        Double somatorioMedias = repository.somatorioProbabilidadeChurn();

        if (totalRegistros == 0 || somatorioMedias == null) {
            return 0.0;
        }

        return somatorioMedias / totalRegistros * 100;
    }
}

