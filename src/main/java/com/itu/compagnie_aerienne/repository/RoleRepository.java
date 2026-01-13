package com.itu.compagnie_aerienne.repository;

import com.itu.compagnie_aerienne.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}
