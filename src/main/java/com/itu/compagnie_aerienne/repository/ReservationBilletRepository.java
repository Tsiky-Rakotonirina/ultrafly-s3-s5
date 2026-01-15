package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.ReservationBillet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationBilletRepository extends JpaRepository<ReservationBillet, Integer> {
    
    List<ReservationBillet> findByReservationIdReservation(Integer reservationId);
    
    List<ReservationBillet> findByReservationIdReservationOrderByIdReservationBilletAsc(Integer reservationId);
    
    List<ReservationBillet> findByReservationVolIdVol(Integer volId);
    
    @Query("SELECT CASE WHEN COUNT(rb) > 0 THEN true ELSE false END FROM ReservationBillet rb " +
           "WHERE rb.avionSiege.idAvionSiege = :siegeId " +
           "AND rb.reservation.vol.idVol = :volId " +
           "AND rb.billetStatut.libelle NOT LIKE '%Annul%'")
    boolean existsBySiegeAndVolNonAnnule(@Param("siegeId") Integer siegeId, @Param("volId") Integer volId);
    
    @Query("SELECT rb.avionSiege.idAvionSiege FROM ReservationBillet rb " +
           "WHERE rb.reservation.vol.idVol = :volId " +
           "AND rb.billetStatut.libelle NOT LIKE '%Annul%'")
    List<Integer> findSiegesReservesByVol(@Param("volId") Integer volId);
}
