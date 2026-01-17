package com.hackathon.continuum.dto;

public record RelatorioAnaliseDTO(
    Long clienteId,
    String nomeCliente,
    Double probabilidadeChurn,
    String risco,
    String acaoRetencao,
    String atributo
) {

}
