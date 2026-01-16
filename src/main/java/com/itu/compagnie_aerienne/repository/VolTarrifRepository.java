package com.itu.compagnie_aerienne.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itu.compagnie_aerienne.model.VolTarrif;

@Repository
public interface VolTarrifRepository extends JpaRepository<VolTarrif, Integer> {
    List<VolTarrif> findAllByVolIdVol(Integer idVol);
    
    @Query("SELECT vt FROM VolTarrif vt WHERE vt.vol.idVol = :volId AND vt.siegeCategorie.idSiegeCategorie = :categorieId")
    Optional<VolTarrif> findByVolAndCategorie(@Param("volId") Integer volId, @Param("categorieId") Integer categorieId);
}
