package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.VolType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VolTypeRepository extends JpaRepository<VolType, Integer> {
}
