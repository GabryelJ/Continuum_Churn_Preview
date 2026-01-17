package com.hackathon.continuum.controller;

import com.hackathon.continuum.infra.filtros.LogTrace;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hackathon.continuum.dto.EntradaDTO;
import com.hackathon.continuum.dto.EntradaModeloDTO;
import com.hackathon.continuum.dto.RespostaDTO;
import com.hackathon.continuum.entity.AnaliseChurn;
import com.hackathon.continuum.entity.ResultadoChurn;
import com.hackathon.continuum.entity.SugestaoRetencaoChurn;
import com.hackathon.continuum.service.AnaliseChurnService;
import com.hackathon.continuum.service.PredictService;
import com.hackathon.continuum.service.ResultadoChurnService;
import com.hackathon.continuum.service.SugestaoRetencaoChurnService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/analises-churn")
public class AnaliseChurnController {

    private final AnaliseChurnService analiseChurnService;
    private final PredictService predictService;
    private final ResultadoChurnService resultadoChurnService;
    private final SugestaoRetencaoChurnService sugestaoService;

    public AnaliseChurnController(
            AnaliseChurnService analiseChurnService,
            PredictService predictService,
            ResultadoChurnService resultadoChurnService,
            SugestaoRetencaoChurnService sugestaoService
    ) {
        this.analiseChurnService = analiseChurnService;
        this.predictService = predictService;
        this.resultadoChurnService = resultadoChurnService;
        this.sugestaoService = sugestaoService;
    }

    @PostMapping
    public ResponseEntity<RespostaDTO> analisar(@Valid @RequestBody EntradaDTO entradaDTO) {
        String traceId = LogTrace.getTraceId();
        // cria e salva a análise
        AnaliseChurn analise = analiseChurnService.criarAnalise(entradaDTO);

        // chama a API Python
        EntradaModeloDTO modeloDTO =
                new EntradaModeloDTO(entradaDTO, traceId, analise.getId().toString());

        RespostaDTO resposta = predictService.predict(modeloDTO);

        // salva o resultado
        ResultadoChurn resultado =
                resultadoChurnService.salvarResultado(analise, resposta);

        // gera sugestão de retenção
        SugestaoRetencaoChurn sugestao =
                sugestaoService.gerarSugestao(
                        analise,
                        resultado,
                        resposta.probabilidade_churn()
                );

        // retorna resposta ao front
        return ResponseEntity.ok(resposta);
    }
}

