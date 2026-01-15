package com.itu.compagnie_aerienne.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itu.compagnie_aerienne.model.MethodePaiement;

@Repository
public interface MethodePaiementRepository extends JpaRepository<MethodePaiement, Integer> {
}