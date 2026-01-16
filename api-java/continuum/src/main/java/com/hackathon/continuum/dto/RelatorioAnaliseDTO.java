package com.hackathon.continuum.dto;

public record RelatorioAnaliseDTO(
    String nomeCliente,
    Double probabilidadeChurn,
    String risco,
    String acaoRetencao,
    String atributo
) {

}
