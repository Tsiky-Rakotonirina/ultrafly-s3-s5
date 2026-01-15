package com.itu.compagnie_aerienne.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itu.compagnie_aerienne.model.SiegeCategorie;

@Repository
public interface SiegeCategorieRepository extends JpaRepository<SiegeCategorie, Integer> {
    public List<SiegeCategorie> findAll();

}
