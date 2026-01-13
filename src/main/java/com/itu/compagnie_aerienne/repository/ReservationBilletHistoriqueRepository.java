package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.ReservationBilletHistorique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationBilletHistoriqueRepository extends JpaRepository<ReservationBilletHistorique, Integer> {
}
