package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.ReservationBillet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationBilletRepository extends JpaRepository<ReservationBillet, Integer> {
}
