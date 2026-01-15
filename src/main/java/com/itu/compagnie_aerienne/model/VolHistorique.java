package com.itu.compagnie_aerienne.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "vol_historique")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VolHistorique {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vol_historique_generator")
    @SequenceGenerator(name = "vol_historique_generator", sequenceName = "seq_vol_historique", allocationSize = 1)
    @Column(name = "id_vol_historique")
    private Integer idVolHistorique;
    
    @Column(name = "date_statut", nullable = false)
    private LocalDate dateStatut;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vol_id", nullable = false)
    private Vol vol;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "statut_vol_id", nullable = false)
    private VolStatut statutVol;
}
