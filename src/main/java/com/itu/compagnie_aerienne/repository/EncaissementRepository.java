package com.itu.compagnie_aerienne.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itu.compagnie_aerienne.model.Encaissement;

@Repository
public interface EncaissementRepository extends JpaRepository<Encaissement, Integer> {
    List<Encaissement> findByPubliciteDiffusionVolIdPubliciteDiffusionVol(Integer publiciteDiffusionVolId);
    
    List<Encaissement> findByPubliciteDiffusionVolPubliciteDiffusionSocieteIdSociete(Integer societeId);
}
