package com.itu.compagnie_aerienne.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itu.compagnie_aerienne.model.Produit;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Integer> {
    public List<Produit> findAll();
}
