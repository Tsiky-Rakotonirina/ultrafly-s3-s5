package com.itu.compagnie_aerienne.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itu.compagnie_aerienne.model.Avion;

@Repository
public interface AvionRepository extends JpaRepository<Avion, Integer> {
}