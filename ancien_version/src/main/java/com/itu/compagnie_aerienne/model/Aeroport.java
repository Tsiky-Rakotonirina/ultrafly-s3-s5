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
@Table(name = "aeroport")
@Data
@NoArgsConstructor
public class Aeroport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code_aeroport", nullable = false, unique = true, length = 10)
    private String codeAeroport;

    @Column(name = "nom", nullable = false, length = 150)
    private String nom;

    @Column(name = "ville", nullable = false, length = 100)
    private String ville;

    @Column(name = "pays", nullable = false, length = 100)
    private String pays;
}
