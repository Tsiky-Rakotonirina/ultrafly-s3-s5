package com.itu.compagnie_aerienne.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "paiement_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaiementDetail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paiement_detail")
    private Integer idPaiementDetail;
    
    @Column(name = "montant", nullable = false, precision = 10, scale = 2)
    private BigDecimal montant;
    
    @Column(name = "date_paiement", nullable = false)
    private LocalDate datePaiement;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paiement_mode_id", nullable = false)
    private PaiementMode paiementMode;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "devise_id", nullable = false)
    private Devise devise;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paiement_id", nullable = false)
    private Paiement paiement;
}
