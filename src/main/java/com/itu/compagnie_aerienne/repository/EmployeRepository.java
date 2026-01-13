package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeRepository extends JpaRepository<Employe, Integer> {
}
