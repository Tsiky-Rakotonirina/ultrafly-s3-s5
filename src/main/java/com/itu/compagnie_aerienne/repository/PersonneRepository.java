package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.Personne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonneRepository extends JpaRepository<Personne, Integer> {
    
    @Query("SELECT p FROM Personne p WHERE " +
           "(:nom IS NULL OR LOWER(p.nom) LIKE LOWER(CONCAT('%', :nom, '%'))) AND " +
           "(:email IS NULL OR LOWER(p.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
           "(:paysId IS NULL OR p.pays.idPays = :paysId)")
    List<Personne> filtrer(@Param("nom") String nom, 
                           @Param("email") String email,
                           @Param("paysId") Integer paysId);
}
