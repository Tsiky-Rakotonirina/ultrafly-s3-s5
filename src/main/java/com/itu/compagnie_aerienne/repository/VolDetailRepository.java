package com.itu.compagnie_aerienne.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itu.compagnie_aerienne.model.VolDetail;

@Repository
public interface VolDetailRepository extends JpaRepository<VolDetail, Integer> {
    public List<VolDetail> findAll();

    public List<VolDetail> findByVolIdVol(Integer idVol);
}
