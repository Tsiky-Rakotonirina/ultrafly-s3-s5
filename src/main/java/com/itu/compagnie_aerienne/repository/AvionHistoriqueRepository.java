package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.AvionHistorique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AvionHistoriqueRepository extends JpaRepository<AvionHistorique, Integer> {
    
    List<AvionHistorique> findByAvionIdAvionOrderByDateStatutDesc(Integer avionId);
    
    @Query("SELECT h FROM AvionHistorique h WHERE h.avion.idAvion = :avionId ORDER BY h.dateStatut DESC, h.idAvionHistorique DESC")
    List<AvionHistorique> findHistoriqueByAvion(@Param("avionId") Integer avionId);
    
    @Query("SELECT h FROM AvionHistorique h WHERE h.avion.idAvion = :avionId ORDER BY h.dateStatut DESC, h.idAvionHistorique DESC LIMIT 1")
    Optional<AvionHistorique> findDernierStatut(@Param("avionId") Integer avionId);
}
