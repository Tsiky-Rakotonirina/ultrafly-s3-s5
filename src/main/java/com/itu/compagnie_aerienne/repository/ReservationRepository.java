package com.itu.compagnie_aerienne.repository;

import java.util.List;

import com.itu.compagnie_aerienne.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    public List<Reservation> findAllByVolIdVol(Integer idVol);
}
