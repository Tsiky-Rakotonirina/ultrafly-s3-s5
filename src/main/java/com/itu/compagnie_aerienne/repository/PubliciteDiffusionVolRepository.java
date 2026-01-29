package com.itu.compagnie_aerienne.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itu.compagnie_aerienne.model.PubliciteDiffusionVol;

@Repository
public interface PubliciteDiffusionVolRepository extends JpaRepository<PubliciteDiffusionVol, Integer> {
    List<PubliciteDiffusionVol> findByVolIdVol(Integer volId);
    
    List<PubliciteDiffusionVol> findByPubliciteDiffusionSocieteIdSociete(Integer societeId);
}
