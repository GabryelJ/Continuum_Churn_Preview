package com.hackathon.continuum.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hackathon.continuum.dto.RelatorioAnaliseDTO;
import com.hackathon.continuum.repository.RelatorioAnaliseRepository;

@Service
public class RelatorioAnaliseService {

    private final RelatorioAnaliseRepository repository;

    public RelatorioAnaliseService(RelatorioAnaliseRepository repository) {
        this.repository = repository;
    }

    public List<RelatorioAnaliseDTO> listarRelatorio() {
        return repository.buscarRelatorioAnalises();
    }
}
