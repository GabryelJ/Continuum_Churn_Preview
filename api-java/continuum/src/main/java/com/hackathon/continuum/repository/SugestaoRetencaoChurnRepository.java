package com.hackathon.continuum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hackathon.continuum.entity.SugestaoRetencaoChurn;

import java.util.Optional;

public interface SugestaoRetencaoChurnRepository extends JpaRepository<SugestaoRetencaoChurn, Long> {

    Optional<SugestaoRetencaoChurn> findByAnaliseChurnId(Long clienteId);

}
