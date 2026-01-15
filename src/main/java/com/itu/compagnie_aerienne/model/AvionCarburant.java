package com.itu.compagnie_aerienne.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "avion_carburant")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvionCarburant {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "avion_carburant_generator")
    @SequenceGenerator(name = "avion_carburant_generator", sequenceName = "seq_avion_carburant", allocationSize = 1)
    @Column(name = "id_avion_carburant")
    private Integer idAvionCarburant;
    
    @Column(name = "quantite", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantite;
    
    @Column(name = "date_carburant", nullable = false)
    private LocalDate dateCarburant;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "avion_id", nullable = false)
    private Avion avion;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "carburant_id", nullable = false)
    private Carburant carburant;
}
