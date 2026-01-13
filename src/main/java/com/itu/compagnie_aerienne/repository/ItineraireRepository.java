package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.Itineraire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItineraireRepository extends JpaRepository<Itineraire, Integer> {
}
