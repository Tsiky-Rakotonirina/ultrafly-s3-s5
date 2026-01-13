package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.BagageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BagageTypeRepository extends JpaRepository<BagageType, Integer> {
}
