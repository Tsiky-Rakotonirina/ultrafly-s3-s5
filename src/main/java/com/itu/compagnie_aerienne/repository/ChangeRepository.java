package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.Change;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChangeRepository extends JpaRepository<Change, Integer> {
    
    Optional<Change> findTopByDeviseIdDeviseOrderByDateChangeDesc(Integer deviseId);
}
