package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.ClientType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientTypeRepository extends JpaRepository<ClientType, Integer> {
}
