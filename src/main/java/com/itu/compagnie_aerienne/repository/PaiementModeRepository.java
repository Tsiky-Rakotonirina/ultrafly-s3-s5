package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.PaiementMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaiementModeRepository extends JpaRepository<PaiementMode, Integer> {
}
