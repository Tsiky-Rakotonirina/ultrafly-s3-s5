package com.itu.compagnie_aerienne.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "aeroport")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Aeroport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aeroport")
    private Integer idAeroport;
    
    @Column(name = "nom", nullable = false, length = 150)
    private String nom;
    
    @Column(name = "code_iata", nullable = false, length = 10)
    private String codeIata;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ville_id", nullable = false)
    private Ville ville;
}
