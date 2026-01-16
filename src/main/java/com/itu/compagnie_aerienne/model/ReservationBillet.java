package com.itu.compagnie_aerienne.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reservation_billet")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationBillet {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reservation_billet_generator")
    @SequenceGenerator(name = "reservation_billet_generator", sequenceName = "seq_reservation_billet", allocationSize = 1)
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
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;
}
