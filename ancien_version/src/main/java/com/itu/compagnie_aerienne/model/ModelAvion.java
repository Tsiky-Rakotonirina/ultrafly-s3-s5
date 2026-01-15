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
@Table(name = "model_avion")
@Data
@NoArgsConstructor
public class ModelAvion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "designation", nullable = false, unique = true, length = 100)
    private String designation;

    @Column(name = "fabricant", nullable = false, length = 100)
    private String fabricant;

    @Column(name = "capacite", nullable = false)
    private Integer capacite;

    @Column(name = "autonomie_km")
    private Integer autonomieKm;

    @Column(name = "vitesse_km_h")
    private Integer vitesseKmH;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
