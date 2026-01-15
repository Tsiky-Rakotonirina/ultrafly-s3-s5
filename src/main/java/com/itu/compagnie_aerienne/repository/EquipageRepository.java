package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.Equipage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EquipageRepository extends JpaRepository<Equipage, Integer> {
    
    @Query("SELECT e FROM Equipage e WHERE " +
           "(:nom IS NULL OR LOWER(e.nom) LIKE LOWER(CONCAT('%', :nom, '%'))) AND " +
           "(:dateDebut IS NULL OR e.dateEquipage >= :dateDebut) AND " +
           "(:dateFin IS NULL OR e.dateEquipage <= :dateFin)")
    List<Equipage> filtrer(@Param("nom") String nom, 
                           @Param("dateDebut") LocalDate dateDebut,
                           @Param("dateFin") LocalDate dateFin);
}
