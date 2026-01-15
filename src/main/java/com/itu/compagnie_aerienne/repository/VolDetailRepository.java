package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.VolDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VolDetailRepository extends JpaRepository<VolDetail, Integer> {
    
    List<VolDetail> findByVolIdVolOrderByHeureAsc(Integer volId);
    
    List<VolDetail> findByAvionIdAvion(Integer avionId);
    
    List<VolDetail> findByEquipageIdEquipage(Integer equipageId);
}
