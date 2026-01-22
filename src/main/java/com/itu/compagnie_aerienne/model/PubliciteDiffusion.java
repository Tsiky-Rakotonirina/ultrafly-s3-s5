package com.itu.compagnie_aerienne.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "publicite_diffusion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PubliciteDiffusion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_publicite_diffusion")
    private Integer idPubliciteDiffusion;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "societe_id", nullable = false)
    private Societe societe;
    
    @Column(name = "mois_annee", nullable = false)
    private LocalDate moisAnnee;
    
    @Column(name = "nombre", nullable = false)
    private Integer nombre;
    
    @Column(name = "duree", precision = 10, scale = 2)
    private BigDecimal duree;
}
