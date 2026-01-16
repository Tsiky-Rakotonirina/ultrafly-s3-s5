package com.itu.compagnie_aerienne.model;

import java.math.BigDecimal;
import java.time.LocalDate;

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
@Table(name = "vol_tarrif_remise")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VolTarrifRemise {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vol_tarrif_remise_generator")
    @SequenceGenerator(name = "vol_tarrif_remise_generator", sequenceName = "seq_vol_tarrif_remise", allocationSize = 1)
    @Column(name = "id_vol_tarrif_remise")
    private Long idVolTarrifRemise;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vol_tarrif_id", nullable = false)
    private VolTarrif volTarrif;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_type_id", nullable = false)
    private ClientType clientType;
    
    @Column(name = "prix", nullable = false, precision = 10, scale = 2)
    private BigDecimal prix;
    
    @Column(name = "date_remise", nullable = false)
    private LocalDate dateRemise;
}
