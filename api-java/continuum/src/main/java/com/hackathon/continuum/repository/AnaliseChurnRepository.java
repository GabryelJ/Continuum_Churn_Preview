package com.hackathon.continuum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hackathon.continuum.entity.AnaliseChurn;
import org.springframework.stereotype.Repository;

@Repository
public interface AnaliseChurnRepository extends JpaRepository<AnaliseChurn, Long> {

}
