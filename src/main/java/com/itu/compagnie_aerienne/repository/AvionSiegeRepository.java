package com.itu.compagnie_aerienne.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itu.compagnie_aerienne.model.AvionSiege;

@Repository
public interface AvionSiegeRepository extends JpaRepository<AvionSiege, Integer> {
    public List<AvionSiege> findByAvionIdAvion(Integer idAvion);
}
