package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.AvionStatut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvionStatutRepository extends JpaRepository<AvionStatut, Integer> {
}
