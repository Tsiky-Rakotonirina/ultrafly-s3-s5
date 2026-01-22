package com.itu.compagnie_aerienne.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itu.compagnie_aerienne.model.PubliciteTarrif;

import java.util.List;

@Repository
public interface PubliciteTarrifRepository extends JpaRepository<PubliciteTarrif, Integer> {
    public List<PubliciteTarrif> findAll();
}
