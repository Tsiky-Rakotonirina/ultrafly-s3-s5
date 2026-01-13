package com.itu.compagnie_aerienne.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "carburant_tarif")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarburantTarif {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carburant_tarif")
    private Integer idCarburantTarif;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "carburant_id", nullable = false)
    private Carburant carburant;
    
    @Column(name = "prix", nullable = false, precision = 10, scale = 2)
    private BigDecimal prix;
    
    @Column(name = "date_tarif", nullable = false)
    private LocalDate dateTarif;
}
