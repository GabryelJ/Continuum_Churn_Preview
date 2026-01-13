package com.hackathon.continuum.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hackathon.continuum.dto.RelatorioAtributoDTO;
import com.hackathon.continuum.repository.RelatorioAtributoRepository;

@Service
public class RelatorioAtributoService {

    private final RelatorioAtributoRepository repository;

    public RelatorioAtributoService(RelatorioAtributoRepository repository) {
        this.repository = repository;
    }

    public List<RelatorioAtributoDTO> gerarRelatorio() {

        Map<String, Long> contador = new HashMap<>();

        somar(contador, repository.buscarPrimeiros());
        somar(contador, repository.buscarSegundos());
        somar(contador, repository.buscarTerceiros());

        return contador.entrySet().stream()
                .map(e -> new RelatorioAtributoDTO(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(RelatorioAtributoDTO::quantidade).reversed())
                .toList();
    }

    private void somar(Map<String, Long> map, List<String> atributos) {
        for (String atributo : atributos) {
            if (atributo != null && !atributo.isBlank()) {
                map.put(atributo, map.getOrDefault(atributo, 0L) + 1);
            }
        }
    }
}
