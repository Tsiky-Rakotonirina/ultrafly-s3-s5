package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.Aeroport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AeroportRepository extends JpaRepository<Aeroport, Integer> {
}
