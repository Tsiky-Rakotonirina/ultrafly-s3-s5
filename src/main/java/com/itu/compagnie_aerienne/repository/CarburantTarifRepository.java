package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.CarburantTarif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarburantTarifRepository extends JpaRepository<CarburantTarif, Integer> {
}
