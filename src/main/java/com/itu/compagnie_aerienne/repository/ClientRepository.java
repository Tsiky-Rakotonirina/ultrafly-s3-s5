package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    
    @Query("SELECT c FROM Client c WHERE " +
           "(:nom IS NULL OR LOWER(c.personne.nom) LIKE LOWER(CONCAT('%', :nom, '%'))) AND " +
           "(:passeport IS NULL OR LOWER(c.passeport) LIKE LOWER(CONCAT('%', :passeport, '%'))) AND " +
           "(:paysId IS NULL OR c.personne.pays.idPays = :paysId)")
    List<Client> filtrer(@Param("nom") String nom, 
                         @Param("passeport") String passeport,
                         @Param("paysId") Integer paysId);
    
    List<Client> findByPersonneIdPersonne(Integer personneId);
}
