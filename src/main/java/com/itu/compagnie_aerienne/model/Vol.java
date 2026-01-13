package com.itu.compagnie_aerienne.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "vol")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vol {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vol")
    private Integer idVol;
    
    @Column(name = "numero", unique = true, length = 10)
    private String numero;
    
    @Column(name = "heure", nullable = false)
    private LocalDateTime heure;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vol_type_id", nullable = false)
    private VolType volType;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "itineraire_id", nullable = false)
    private Itineraire itineraire;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "statut_vol_id", nullable = false)
    private VolStatut statutVol;
}
