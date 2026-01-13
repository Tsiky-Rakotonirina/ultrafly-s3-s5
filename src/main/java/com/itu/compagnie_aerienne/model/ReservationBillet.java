package com.itu.compagnie_aerienne.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "reservation_billet")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationBillet {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reservation_billet")
    private Integer idReservationBillet;
    
    @Column(name = "numero", unique = true, length = 10)
    private String numero;
    
    @Column(name = "prix", nullable = false, precision = 10, scale = 2)
    private BigDecimal prix;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "avion_siege_id", nullable = false)
    private AvionSiege avionSiege;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "billet_statut_id", nullable = false)
    private BilletStatut billetStatut;
}
