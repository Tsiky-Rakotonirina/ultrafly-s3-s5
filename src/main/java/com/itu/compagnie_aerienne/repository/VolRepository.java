package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.Vol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VolRepository extends JpaRepository<Vol, Integer> {
    
    Optional<Vol> findByNumero(String numero);
    
    List<Vol> findByStatutVolIdVolStatut(Integer statutId);
    
    List<Vol> findByVolTypeIdVolType(Integer typeId);
    
    @Query("SELECT v FROM Vol v WHERE " +
           "(:heureDebut IS NULL OR v.heure >= :heureDebut) AND " +
           "(:heureFin IS NULL OR v.heure <= :heureFin) AND " +
           "(:volTypeId IS NULL OR v.volType.idVolType = :volTypeId) AND " +
           "(:statutVolId IS NULL OR v.statutVol.idVolStatut = :statutVolId) AND " +
           "(:itineraireId IS NULL OR v.itineraire.idItineraire = :itineraireId) AND " +
           "(:aeroportDepartId IS NULL OR v.itineraire.aeroportDepart.idAeroport = :aeroportDepartId) AND " +
           "(:aeroportArriveeId IS NULL OR v.itineraire.aeroportArrive.idAeroport = :aeroportArriveeId)")
    List<Vol> filtrer(
            @Param("heureDebut") LocalDateTime heureDebut,
            @Param("heureFin") LocalDateTime heureFin,
            @Param("volTypeId") Integer volTypeId,
            @Param("statutVolId") Integer statutVolId,
            @Param("itineraireId") Integer itineraireId,
            @Param("aeroportDepartId") Integer aeroportDepartId,
            @Param("aeroportArriveeId") Integer aeroportArriveeId
    );
    
    // Vols disponibles pour réservation (statuts: Programmé, En cours)
    @Query("SELECT v FROM Vol v WHERE v.statutVol.libelle IN ('Programme', 'Programmé', 'En cours')")
    List<Vol> findVolsDisponiblesPourReservation();
}
