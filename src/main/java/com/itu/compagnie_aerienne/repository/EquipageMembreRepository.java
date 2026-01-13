package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.EquipageMembre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipageMembreRepository extends JpaRepository<EquipageMembre, Integer> {
}
