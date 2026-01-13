package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.Poste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PosteRepository extends JpaRepository<Poste, Integer> {
}
