package com.itu.compagnie_aerienne.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itu.compagnie_aerienne.model.VolTarrif;

import java.util.List;

@Repository
public interface VolTarrifRepository extends JpaRepository<VolTarrif, Integer> {
    public VolTarrif findByVolIdVolAndSiegeCategorieIdSiegeCategorie(Integer idVol, Integer idSiegeCategorie);

    public List<VolTarrif> findAllByVolIdVol(Integer idVol);
}
