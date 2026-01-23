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
@Table(name = "encaissement_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EncaissementDetail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_encaissement_detail")
    private Integer idEncaissementDetail;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "encaissement_id")
    private Encaissement encaissement;
    
    @Column(name = "montant", nullable = false, precision = 15, scale = 2)
    private BigDecimal montant;
    
    @Column(name = "date", nullable = false)
    private LocalDate date;
}
