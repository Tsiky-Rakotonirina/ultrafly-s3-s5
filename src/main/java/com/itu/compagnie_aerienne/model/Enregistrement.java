package com.itu.compagnie_aerienne.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "enregistrement")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enregistrement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_enregistrement")
    private Integer idEnregistrement;
    
    @Column(name = "numero", unique = true, length = 10)
    private String numero;
    
    @Column(name = "heure_enregistrement", nullable = false)
    private LocalDateTime heureEnregistrement;
    
    @Column(name = "statut_enregistrement", nullable = false, length = 50)
    private String statutEnregistrement;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_type_id", nullable = false)
    private ClientType clientType;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reservation_billet_id", nullable = false)
    private ReservationBillet reservationBillet;
}
