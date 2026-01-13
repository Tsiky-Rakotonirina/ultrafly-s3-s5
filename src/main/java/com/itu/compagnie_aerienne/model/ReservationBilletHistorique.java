package com.itu.compagnie_aerienne.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "reservation_billet_historique")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationBilletHistorique {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reservation_billet_historique")
    private Integer idReservationBilletHistorique;
    
    @Column(name = "date_statut", nullable = false)
    private LocalDate dateStatut;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reservation_billet_id", nullable = false)
    private ReservationBillet reservationBillet;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "billet_statut_id", nullable = false)
    private BilletStatut billetStatut;
}
