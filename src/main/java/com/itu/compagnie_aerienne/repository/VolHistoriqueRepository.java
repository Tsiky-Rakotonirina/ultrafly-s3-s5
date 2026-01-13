package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.VolHistorique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VolHistoriqueRepository extends JpaRepository<VolHistorique, Integer> {
}
