package com.itu.compagnie_aerienne.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itu.compagnie_aerienne.model.PubliciteDiffusion;

@Repository
public interface PubliciteDiffusionRepository extends JpaRepository<PubliciteDiffusion, Integer> {
}
