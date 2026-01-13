package com.itu.compagnie_aerienne.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "avion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Avion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_avion")
    private Integer idAvion;
    
    @Column(name = "numero", unique = true, length = 10)
    private String numero;
    
    @Column(name = "modele", nullable = false, length = 100)
    private String modele;
    
    @Column(name = "constructeur", length = 100)
    private String constructeur;
    
    @Column(name = "consommation", precision = 10, scale = 2)
    private BigDecimal consommation;
    
    @Column(name = "vitesse", precision = 10, scale = 2)
    private BigDecimal vitesse;
    
    @Column(name = "capacite")
    private Integer capacite;
    
    @Column(name = "date_possession")
    private LocalDate datePossession;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "carburant_id")
    private Carburant carburant;
}
