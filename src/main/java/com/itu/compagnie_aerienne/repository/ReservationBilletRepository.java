package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.ReservationBillet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationBilletRepository extends JpaRepository<ReservationBillet, Integer> {
    List<ReservationBillet> findByReservationIdReservationIn(List<Integer> idsReservation);
}
