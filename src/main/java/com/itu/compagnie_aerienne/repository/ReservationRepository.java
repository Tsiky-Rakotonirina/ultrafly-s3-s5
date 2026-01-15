package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    
    @Query("SELECT r FROM Reservation r WHERE " +
           "(:numero IS NULL OR LOWER(r.numero) LIKE LOWER(CONCAT('%', :numero, '%'))) AND " +
           "(:clientId IS NULL OR r.client.idClient = :clientId) AND " +
           "(:volId IS NULL OR r.vol.idVol = :volId) AND " +
           "(:statutId IS NULL OR r.reservationStatut.idReservationStatut = :statutId) AND " +
           "(:dateDebut IS NULL OR r.dateReservation >= :dateDebut) AND " +
           "(:dateFin IS NULL OR r.dateReservation <= :dateFin)")
    List<Reservation> filtrer(@Param("numero") String numero,
                              @Param("clientId") Integer clientId,
                              @Param("volId") Integer volId,
                              @Param("statutId") Integer statutId,
                              @Param("dateDebut") LocalDate dateDebut,
                              @Param("dateFin") LocalDate dateFin);
    
    List<Reservation> findByClientIdClientOrderByDateReservationDesc(Integer clientId);
    
    List<Reservation> findByVolIdVolOrderByDateReservationDesc(Integer volId);
}
