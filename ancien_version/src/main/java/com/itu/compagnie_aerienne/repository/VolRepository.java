package com.itu.compagnie_aerienne.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itu.compagnie_aerienne.model.Vol;

@Repository
public interface VolRepository extends JpaRepository<Vol, Integer> {
    public List<Vol> findAll();
}