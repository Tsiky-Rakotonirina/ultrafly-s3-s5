package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeRepository extends JpaRepository<Employe, Integer> {
    
    @Query("SELECT e FROM Employe e WHERE " +
           "(:nom IS NULL OR LOWER(e.personne.nom) LIKE LOWER(CONCAT('%', :nom, '%'))) AND " +
           "(:posteId IS NULL OR e.poste.idPoste = :posteId) AND " +
           "(:paysId IS NULL OR e.personne.pays.idPays = :paysId)")
    List<Employe> filtrer(@Param("nom") String nom, 
                          @Param("posteId") Integer posteId,
                          @Param("paysId") Integer paysId);
    
    List<Employe> findByPersonneIdPersonne(Integer personneId);
    
    List<Employe> findByPosteIdPoste(Integer posteId);
}
