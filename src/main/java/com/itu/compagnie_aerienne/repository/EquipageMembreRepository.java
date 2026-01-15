package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.EquipageMembre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipageMembreRepository extends JpaRepository<EquipageMembre, Integer> {
    
    List<EquipageMembre> findByEquipageIdEquipageOrderByOrdreAsc(Integer equipageId);
    
    List<EquipageMembre> findByEmployeIdEmploye(Integer employeId);
}
