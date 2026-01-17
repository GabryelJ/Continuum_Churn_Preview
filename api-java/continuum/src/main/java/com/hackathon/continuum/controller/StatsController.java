package com.hackathon.continuum.controller;

import com.hackathon.continuum.dto.StatsDTO;
import com.hackathon.continuum.repository.ResultadoChurnRepository;
import com.hackathon.continuum.service.ResultadoChurnService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats")
public class StatsController {

    ResultadoChurnRepository resultadoChurnRepository;
    ResultadoChurnService resultadoChurnService;

    public StatsController(ResultadoChurnRepository resultadoChurnRepository, ResultadoChurnService resultadoChurnService) {
        this.resultadoChurnRepository = resultadoChurnRepository;
        this.resultadoChurnService = resultadoChurnService;
    }

    @GetMapping
    public ResponseEntity<StatsDTO> stats(){

        Integer quantidade_avaliados = Math.toIntExact(resultadoChurnService.obterTotalRegistros());

        double percentualChurnAlto = resultadoChurnService.obterTaxaDeClientesComRiscoAltoEmPorcentagem();

        double taxaMediaChurn =  resultadoChurnService.obterMediaDasTaxasDeRiscoEmPorcentagem();

        StatsDTO stats = new StatsDTO(quantidade_avaliados, taxaMediaChurn, percentualChurnAlto);

        return ResponseEntity.ok(stats);
    }
}
