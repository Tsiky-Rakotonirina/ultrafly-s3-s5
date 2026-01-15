package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.VolReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VolReportRepository extends JpaRepository<VolReport, Integer> {
    
    List<VolReport> findByVolDetailIdVolDetailOrderByHeureDesc(Integer volDetailId);
}
