package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.ReservationHistorique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationHistoriqueRepository extends JpaRepository<ReservationHistorique, Integer> {
}
