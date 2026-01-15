package com.itu.compagnie_aerienne.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itu.compagnie_aerienne.model.EtatAvion;

@Repository
public interface EtatAvionRepository extends JpaRepository<EtatAvion, Integer> {
}