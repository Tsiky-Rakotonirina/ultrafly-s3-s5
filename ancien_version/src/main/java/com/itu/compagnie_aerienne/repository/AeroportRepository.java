package com.itu.compagnie_aerienne.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itu.compagnie_aerienne.model.Aeroport;

@Repository
public interface AeroportRepository extends JpaRepository<Aeroport, Integer> {
}