package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.AvionCarburant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AvionCarburantRepository extends JpaRepository<AvionCarburant, Integer> {
    
    List<AvionCarburant> findByAvionIdAvionOrderByDateCarburantDesc(Integer avionId);
    
    @Query("SELECT COALESCE(SUM(ac.quantite), 0) FROM AvionCarburant ac WHERE ac.avion.idAvion = :avionId")
    BigDecimal getTotalCarburant(@Param("avionId") Integer avionId);
    
    @Query("SELECT ac FROM AvionCarburant ac WHERE ac.avion.idAvion = :avionId ORDER BY ac.dateCarburant DESC")
    List<AvionCarburant> findRavitaillementsByAvion(@Param("avionId") Integer avionId);
}
