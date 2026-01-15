package com.itu.compagnie_aerienne.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "itineraire_escale")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItineraireEscale {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "itineraire_escale_generator")
    @SequenceGenerator(name = "itineraire_escale_generator", sequenceName = "seq_itineraire_escale", allocationSize = 1)
    @Column(name = "id_itineraire_escale")
    private Integer idItineraireEscale;
    
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
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "itineraire_id", nullable = false)
    private Itineraire itineraire;
}
