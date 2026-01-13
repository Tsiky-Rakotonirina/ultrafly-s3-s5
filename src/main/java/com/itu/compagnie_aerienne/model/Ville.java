package com.itu.compagnie_aerienne.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ville")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ville {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ville")
    private Integer idVille;
    
    @Column(name = "nom", nullable = false, length = 100)
    private String nom;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pays_id", nullable = false)
    private Pays pays;
}
