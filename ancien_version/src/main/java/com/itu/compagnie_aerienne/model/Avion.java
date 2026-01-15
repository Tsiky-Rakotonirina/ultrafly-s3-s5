package com.itu.compagnie_aerienne.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "avion")
@Data
@NoArgsConstructor
public class Avion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code_avion", nullable = false, unique = true, length = 50)
    private String codeAvion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "model_avion_id", nullable = false)
    private ModelAvion modelAvion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "etat_avion_id", nullable = false)
    private EtatAvion etatAvion;

    @Column(name = "capacite_totale", nullable = false)
    private Integer capaciteTotale;
}
