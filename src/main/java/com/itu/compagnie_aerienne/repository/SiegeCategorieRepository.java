package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.SiegeCategorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiegeCategorieRepository extends JpaRepository<SiegeCategorie, Integer> {
}
