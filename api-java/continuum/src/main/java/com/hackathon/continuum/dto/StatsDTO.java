package com.hackathon.continuum.dto;

public record StatsDTO(
        Integer totalAvaliados,
        Double taxaMediaChurn,
        Double percentualDeResultadosComRiscoDeChurnAlto
        ) {
}
