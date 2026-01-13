package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.AvionCarburant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvionCarburantRepository extends JpaRepository<AvionCarburant, Integer> {
}
