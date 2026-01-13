package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.Carburant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarburantRepository extends JpaRepository<Carburant, Integer> {
}
