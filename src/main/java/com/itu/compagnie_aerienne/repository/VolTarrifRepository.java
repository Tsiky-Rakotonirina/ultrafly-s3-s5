package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.VolTarrif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VolTarrifRepository extends JpaRepository<VolTarrif, Integer> {
    
    List<VolTarrif> findByVolIdVol(Integer volId);
    
    Optional<VolTarrif> findByVolIdVolAndSiegeCategorieIdSiegeCategorie(Integer volId, Integer siegeCategorieId);
}
