package com.itu.compagnie_aerienne.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itu.compagnie_aerienne.model.Encaissement;

@Repository
public interface EncaissementRepository extends JpaRepository<Encaissement, Integer> {
    
    List<Encaissement> findBySocieteIdSociete(Integer societeId);
    
    @Query("SELECT COALESCE(SUM(e.montant), 0) FROM Encaissement e WHERE e.societe.idSociete = :societeId")
    BigDecimal sumMontantBySocieteId(@Param("societeId") Integer societeId);
    
    @Query("SELECT COALESCE(SUM(e.montant), 0) FROM Encaissement e WHERE e.societe.idSociete = :societeId AND e.dateEncaissement <= :dateFin")
    BigDecimal sumMontantBySocieteIdAndDateBefore(@Param("societeId") Integer societeId, @Param("dateFin") LocalDate dateFin);
}
