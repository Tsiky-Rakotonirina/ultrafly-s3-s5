package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.BilletStatut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BilletStatutRepository extends JpaRepository<BilletStatut, Integer> {
    
    Optional<BilletStatut> findByLibelle(String libelle);
}
