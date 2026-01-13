package com.itu.compagnie_aerienne.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "avion_historique")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvionHistorique {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_avion_historique")
    private Integer idAvionHistorique;
    
    @Column(name = "date_statut", nullable = false)
    private LocalDate dateStatut;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "avion_id", nullable = false)
    private Avion avion;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "avion_statut_id", nullable = false)
    private AvionStatut avionStatut;
}
