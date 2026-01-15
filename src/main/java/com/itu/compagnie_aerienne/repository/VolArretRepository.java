package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.VolArret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VolArretRepository extends JpaRepository<VolArret, Integer> {
    int countByVolIdVol(Integer volId);
}
