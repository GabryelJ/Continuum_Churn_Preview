package com.hackathon.continuum.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hackathon.continuum.dto.RelatorioAnaliseDTO;
import com.hackathon.continuum.service.RelatorioAnaliseService;

@RestController
@RequestMapping("/analises-churn/relatorios")
public class RelatorioAnaliseController {

    private final RelatorioAnaliseService service;

    public RelatorioAnaliseController(RelatorioAnaliseService service) {
        this.service = service;
    }

    @GetMapping("/analises")
    public ResponseEntity<List<RelatorioAnaliseDTO>> listar() {
        return ResponseEntity.ok(service.listarRelatorio());
    }
}
