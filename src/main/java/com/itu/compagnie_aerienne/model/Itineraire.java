package com.itu.compagnie_aerienne.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "itineraire")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Itineraire {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "itineraire_generator")
    @SequenceGenerator(name = "itineraire_generator", sequenceName = "seq_itineraire", allocationSize = 1)
    @Column(name = "id_itineraire")
    private Integer idItineraire;
    
    @Column(name = "numero", unique = true, length = 10)
    private String numero;
    
    @Column(name = "duree", columnDefinition = "INTERVAL")
    private String duree;
    
    @Column(name = "distance", precision = 10, scale = 2)
    private BigDecimal distance;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "aeroport_depart_id", nullable = false)
    private Aeroport aeroportDepart;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "aeroport_arrive_id", nullable = false)
    private Aeroport aeroportArrive;
}
