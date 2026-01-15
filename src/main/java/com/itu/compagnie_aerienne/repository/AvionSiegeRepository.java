package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.AvionSiege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvionSiegeRepository extends JpaRepository<AvionSiege, Integer> {
    
    List<AvionSiege> findByAvionIdAvion(Integer avionId);
    
    List<AvionSiege> findByAvionIdAvionAndSiegeCategorieIdSiegeCategorie(Integer avionId, Integer siegeCategorieId);
    
    List<AvionSiege> findByAvionIdAvionOrderByRangeeAscColonneAsc(Integer avionId);
    
    @Query("SELECT s FROM AvionSiege s WHERE s.avion.idAvion = :avionId ORDER BY s.rangee, s.colonne")
    List<AvionSiege> findSiegesOrderedByPosition(@Param("avionId") Integer avionId);
    
    @Query("SELECT s FROM AvionSiege s WHERE s.avion.idAvion = :avionId ORDER BY s.siegeCategorie.idSiegeCategorie, s.rangee, s.colonne")
    List<AvionSiege> findByAvionIdAvionOrderBySiegeCategorieIdSiegeCategorieAscRangeeAscColonneAsc(@Param("avionId") Integer avionId);
    
    @Query("SELECT MAX(s.rangee) FROM AvionSiege s WHERE s.avion.idAvion = :avionId")
    Integer getMaxRangee(@Param("avionId") Integer avionId);
    
    @Query("SELECT COUNT(s) FROM AvionSiege s WHERE s.avion.idAvion = :avionId")
    Integer countByAvionId(@Param("avionId") Integer avionId);
    
    void deleteByAvionIdAvion(Integer avionId);
}
