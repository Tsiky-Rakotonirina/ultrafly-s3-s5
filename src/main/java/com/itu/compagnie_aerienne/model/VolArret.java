package com.itu.compagnie_aerienne.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "vol_arret")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VolArret {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vol_arret_generator")
    @SequenceGenerator(name = "vol_arret_generator", sequenceName = "seq_vol_arret", allocationSize = 1)
    @Column(name = "id_vol_arret")
    private Integer idVolArret;
    
    @Column(name = "numero", unique = true, length = 10)
    private String numero;
    
    @Column(name = "heure", nullable = false)
    private LocalDateTime heure;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "aeroport_id", nullable = false)
    private Aeroport aeroport;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vol_id", nullable = false)
    private Vol vol;
}
