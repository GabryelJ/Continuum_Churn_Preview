package com.hackathon.continuum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hackathon.continuum.entity.SugestaoRetencaoChurn;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SugestaoRetencaoChurnRepository extends JpaRepository<SugestaoRetencaoChurn, Long> {

    Optional<SugestaoRetencaoChurn> findByAnaliseChurnId(Long clienteId);

}
