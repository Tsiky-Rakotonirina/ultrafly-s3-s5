package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.PaiementDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaiementDetailRepository extends JpaRepository<PaiementDetail, Integer> {
}
