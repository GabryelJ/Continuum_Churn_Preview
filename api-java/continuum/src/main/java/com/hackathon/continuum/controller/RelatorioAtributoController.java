package com.hackathon.continuum.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hackathon.continuum.dto.RelatorioAtributoDTO;
import com.hackathon.continuum.service.RelatorioAtributoService;

@RestController
@RequestMapping("/analises-churn/relatorios")
public class RelatorioAtributoController {
    
    private final RelatorioAtributoService service;

    public RelatorioAtributoController(RelatorioAtributoService service) {
        this.service = service;
    }

    @GetMapping("/atributos")
    public List<RelatorioAtributoDTO> listarAtributos() {
        return service.gerarRelatorio();
    }

}
