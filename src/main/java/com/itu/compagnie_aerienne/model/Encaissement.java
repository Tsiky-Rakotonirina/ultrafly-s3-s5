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
@Table(name = "encaissement")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Encaissement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_encaissement")
    private Integer idEncaissement;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "publicite_diffusion_id")
    private PubliciteDiffusion publiciteDiffusion;
    
    @Column(name = "date_encaissement", nullable = false)
    private LocalDate dateEncaissement;
    
    @Column(name = "montant", nullable = false, precision = 15, scale = 2)
    private BigDecimal montant;
    
    @Column(name = "reste_a_payer", nullable = false, precision = 15, scale = 2)
    private BigDecimal resteAPayer;
}
