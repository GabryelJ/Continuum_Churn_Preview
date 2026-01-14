package com.hackathon.continuum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hackathon.continuum.entity.ResultadoChurn;

public interface RelatorioAtributoRepository extends JpaRepository<ResultadoChurn, Long> {

    @Query("SELECT r.primeiroMaisRelevante FROM ResultadoChurn r")
    List<String> buscarPrimeiros();

    @Query("SELECT r.segundoMaisRelevante FROM ResultadoChurn r")
    List<String> buscarSegundos();

    @Query("SELECT r.terceiroMaisRelevante FROM ResultadoChurn r")
    List<String> buscarTerceiros();
}


