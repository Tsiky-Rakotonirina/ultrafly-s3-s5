package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.Pays;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaysRepository extends JpaRepository<Pays, Integer> {
}
