package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.ItineraireEscale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItineraireEscaleRepository extends JpaRepository<ItineraireEscale, Integer> {
    
    List<ItineraireEscale> findByItineraireIdItineraireOrderByIdItineraireEscaleAsc(Integer itineraireId);
}
