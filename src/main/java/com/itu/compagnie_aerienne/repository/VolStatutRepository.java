package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.VolStatut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VolStatutRepository extends JpaRepository<VolStatut, Integer> {
}
