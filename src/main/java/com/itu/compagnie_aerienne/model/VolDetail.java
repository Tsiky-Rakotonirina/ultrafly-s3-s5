package com.itu.compagnie_aerienne.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "vol_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VolDetail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vol_detail_generator")
    @SequenceGenerator(name = "vol_detail_generator", sequenceName = "seq_vol_detail", allocationSize = 1)
    @Column(name = "id_vol_detail")
    private Integer idVolDetail;
    
    @Column(name = "heure", nullable = false)
    private LocalDateTime heure;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vol_id", nullable = false)
    private Vol vol;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "itineraire_escale_id")
    private ItineraireEscale itineraireEscale;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "avion_id")
    private Avion avion;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "equipage_id")
    private Equipage equipage;
}
