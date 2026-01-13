package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.ReservationStatut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationStatutRepository extends JpaRepository<ReservationStatut, Integer> {
}
