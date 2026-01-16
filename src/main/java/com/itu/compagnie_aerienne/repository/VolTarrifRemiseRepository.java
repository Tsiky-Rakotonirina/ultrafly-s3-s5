package com.itu.compagnie_aerienne.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itu.compagnie_aerienne.model.VolTarrifRemise;

@Repository
public interface VolTarrifRemiseRepository extends JpaRepository<VolTarrifRemise, Long> {
    
    List<VolTarrifRemise> findByVolTarrifIdVolTarrif(Integer idVolTarrif);
    
    List<VolTarrifRemise> findByDateRemise(LocalDate dateRemise);
    
    List<VolTarrifRemise> findByDateRemiseBetween(LocalDate dateDebut, LocalDate dateFin);
    
    List<VolTarrifRemise> findByVolTarrifIdVolTarrifAndDateRemise(Integer idVolTarrif, LocalDate dateRemise);
}
