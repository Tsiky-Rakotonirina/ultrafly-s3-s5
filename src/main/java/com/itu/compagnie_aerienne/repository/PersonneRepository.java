package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.Personne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonneRepository extends JpaRepository<Personne, Integer> {
}
