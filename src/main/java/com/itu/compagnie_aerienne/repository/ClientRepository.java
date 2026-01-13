package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
}
