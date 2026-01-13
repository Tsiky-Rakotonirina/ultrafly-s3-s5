package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.Equipage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipageRepository extends JpaRepository<Equipage, Integer> {
}
