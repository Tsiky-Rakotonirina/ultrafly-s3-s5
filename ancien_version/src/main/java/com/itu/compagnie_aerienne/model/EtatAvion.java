package com.itu.compagnie_aerienne.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "etat_avion")
@Data
@NoArgsConstructor
public class EtatAvion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "libelle", nullable = false, unique = true, length = 50)
    private String libelle;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
