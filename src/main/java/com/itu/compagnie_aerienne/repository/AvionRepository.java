package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.Avion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvionRepository extends JpaRepository<Avion, Integer> {
}
