package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.EnregistrementBagage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnregistrementBagageRepository extends JpaRepository<EnregistrementBagage, Integer> {
}
