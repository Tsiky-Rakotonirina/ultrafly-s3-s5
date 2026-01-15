package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.Avion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvionRepository extends JpaRepository<Avion, Integer> {
    
    Optional<Avion> findByNumero(String numero);
    
    List<Avion> findByConstructeurContainingIgnoreCase(String constructeur);
    
    List<Avion> findByModeleContainingIgnoreCase(String modele);
    
    List<Avion> findByCarburantIdCarburant(Integer carburantId);
    
    @Query("SELECT a FROM Avion a WHERE " +
           "(:constructeur IS NULL OR LOWER(a.constructeur) LIKE LOWER(CONCAT('%', :constructeur, '%'))) AND " +
           "(:modele IS NULL OR LOWER(a.modele) LIKE LOWER(CONCAT('%', :modele, '%'))) AND " +
           "(:capaciteMin IS NULL OR a.capacite >= :capaciteMin) AND " +
           "(:capaciteMax IS NULL OR a.capacite <= :capaciteMax) AND " +
           "(:consommationMin IS NULL OR a.consommation >= :consommationMin) AND " +
           "(:consommationMax IS NULL OR a.consommation <= :consommationMax) AND " +
           "(:datePossessionDebut IS NULL OR a.datePossession >= :datePossessionDebut) AND " +
           "(:datePossessionFin IS NULL OR a.datePossession <= :datePossessionFin) AND " +
           "(:carburantId IS NULL OR a.carburant.idCarburant = :carburantId)")
    List<Avion> filtrer(
            @Param("constructeur") String constructeur,
            @Param("modele") String modele,
            @Param("capaciteMin") Integer capaciteMin,
            @Param("capaciteMax") Integer capaciteMax,
            @Param("consommationMin") BigDecimal consommationMin,
            @Param("consommationMax") BigDecimal consommationMax,
            @Param("datePossessionDebut") LocalDate datePossessionDebut,
            @Param("datePossessionFin") LocalDate datePossessionFin,
            @Param("carburantId") Integer carburantId
    );
}
