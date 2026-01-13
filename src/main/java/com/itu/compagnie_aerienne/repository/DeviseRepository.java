package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.Devise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviseRepository extends JpaRepository<Devise, Integer> {
}
