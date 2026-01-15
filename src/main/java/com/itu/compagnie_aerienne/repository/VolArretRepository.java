package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.VolArret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VolArretRepository extends JpaRepository<VolArret, Integer> {
    
    List<VolArret> findByVolIdVolOrderByHeureAsc(Integer volId);
}
