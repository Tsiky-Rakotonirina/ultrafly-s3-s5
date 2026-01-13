package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.AvionSiege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvionSiegeRepository extends JpaRepository<AvionSiege, Integer> {
}
