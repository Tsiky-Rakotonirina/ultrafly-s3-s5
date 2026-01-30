package com.itu.compagnie_aerienne.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itu.compagnie_aerienne.model.ProduitVenteDetail;

@Repository
public interface ProduitVenteDetailRepository extends JpaRepository<ProduitVenteDetail, Integer> {
    List<ProduitVenteDetail> findByProduitVenteIdProduitVente(Integer idProduitVente);
}
