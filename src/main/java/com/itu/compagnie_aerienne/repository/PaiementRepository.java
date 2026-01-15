package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Integer> {
    public Paiement findByReservationIdReservation(Integer idReservation);
    
    public List<Paiement> findByReservationIdReservationIn(List<Integer> idsReservation);
}
