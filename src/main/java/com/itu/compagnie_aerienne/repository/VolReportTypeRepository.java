package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.VolReportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VolReportTypeRepository extends JpaRepository<VolReportType, Integer> {
}
