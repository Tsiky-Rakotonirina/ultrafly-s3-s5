package com.itu.compagnie_aerienne.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itu.compagnie_aerienne.model.Paiement;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Integer> {
}