package com.itu.compagnie_aerienne.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "publicite_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PubliciteType {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_publicite_type")
    private Integer idPubliciteType;
    
    @Column(name = "libelle", nullable = false, length = 100)
    private String libelle;
    
    @Column(name = "duree_min", nullable = false)
    private Integer dureeMin;
    
    @Column(name = "duree_max")
    private Integer dureeMax;
}
