package com.hackathon.continuum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hackathon.continuum.entity.ResultadoChurn;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ResultadoChurnRepository extends JpaRepository<ResultadoChurn, Long> {

    long countByRisco(String risco);

    @Query("SELECT SUM(r.probabilidadeChurn) FROM ResultadoChurn r")
    Double somatorioProbabilidadeChurn();
}
