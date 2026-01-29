package com.itu.compagnie_aerienne.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itu.compagnie_aerienne.model.EncaissementDetail;

@Repository
public interface EncaissementDetailRepository extends JpaRepository<EncaissementDetail, Integer> {
    List<EncaissementDetail> findByEncaissementIdEncaissement(Integer encaissementId);
}
