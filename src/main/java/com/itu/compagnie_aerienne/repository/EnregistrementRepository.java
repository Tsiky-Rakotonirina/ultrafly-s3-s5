package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.Enregistrement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnregistrementRepository extends JpaRepository<Enregistrement, Integer> {
}
