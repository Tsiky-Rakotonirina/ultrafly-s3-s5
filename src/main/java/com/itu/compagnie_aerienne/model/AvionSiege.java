package com.itu.compagnie_aerienne.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "avion_siege")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvionSiege {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_avion_siege")
    private Integer idAvionSiege;
    
    @Column(name = "numero", unique = true, length = 10)
    private String numero;
    
    @Column(name = "colonne", nullable = false, length = 1)
    private String colonne;
    
    @Column(name = "rangee", nullable = false)
    private Integer rangee;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "siege_categorie_id", nullable = false)
    private SiegeCategorie siegeCategorie;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "avion_id", nullable = false)
    private Avion avion;
}
