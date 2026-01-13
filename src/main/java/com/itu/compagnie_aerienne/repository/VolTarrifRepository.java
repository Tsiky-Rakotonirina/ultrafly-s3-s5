package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.VolTarrif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VolTarrifRepository extends JpaRepository<VolTarrif, Integer> {
}
