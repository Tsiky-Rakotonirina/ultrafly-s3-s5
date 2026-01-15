package com.itu.compagnie_aerienne.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itu.compagnie_aerienne.model.PrixVol;

@Repository
public interface PrixVolRepository extends JpaRepository<PrixVol, Integer> {
    public List<PrixVol> findByVolId(Integer volId);
    
    public Optional<PrixVol> findByVolIdAndClasseSiegeId(Integer volId, Integer classeSiegeId);

}