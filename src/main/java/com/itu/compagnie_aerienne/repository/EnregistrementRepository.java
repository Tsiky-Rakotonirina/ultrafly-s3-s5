package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.Enregistrement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EnregistrementRepository extends JpaRepository<Enregistrement, Integer> {
    
    List<Enregistrement> findByVolIdVolOrderByIdEnregistrementAsc(Integer volId);
    
    List<Enregistrement> findByReservationBilletReservationIdReservationOrderByIdEnregistrementAsc(Integer reservationId);
    
    @Query("SELECT e FROM Enregistrement e WHERE " +
           "(:numero IS NULL OR e.numero = :numero) AND " +
           "(:volId IS NULL OR e.reservationBillet.reservation.vol.idVol = :volId) AND " +
           "(:reservationId IS NULL OR e.reservationBillet.reservation.idReservation = :reservationId) AND " +
           "(:heureDebut IS NULL OR e.heureEnregistrement >= :heureDebut) AND " +
           "(:heureFin IS NULL OR e.heureEnregistrement <= :heureFin) AND " +
           "(:status IS NULL OR e.statutEnregistrement = :status)")
    List<Enregistrement> filtrer(
            @Param("numero") String numero,
            @Param("volId") Integer volId,
            @Param("reservationId") Integer reservationId,
            @Param("heureDebut") LocalDateTime heureDebut,
            @Param("heureFin") LocalDateTime heureFin,
            @Param("status") String status
    );
}
