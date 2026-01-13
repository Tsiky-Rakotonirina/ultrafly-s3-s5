package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.AvionHistorique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvionHistoriqueRepository extends JpaRepository<AvionHistorique, Integer> {
}
