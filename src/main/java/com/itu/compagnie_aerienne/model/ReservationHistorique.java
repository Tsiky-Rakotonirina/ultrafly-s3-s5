package com.itu.compagnie_aerienne.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "reservation_historique")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationHistorique {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reservation_historique_generator")
    @SequenceGenerator(name = "reservation_historique_generator", sequenceName = "seq_reservation_historique", allocationSize = 1)
    @Column(name = "id_reservation_historique")
    private Integer idReservationHistorique;
    
    @Column(name = "date_statut", nullable = false)
    private LocalDate dateStatut;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reservation_statut_id", nullable = false)
    private ReservationStatut reservationStatut;
}
