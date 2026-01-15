package com.itu.compagnie_aerienne.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "vol_tarrif")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VolTarrif {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vol_tarrif_generator")
    @SequenceGenerator(name = "vol_tarrif_generator", sequenceName = "seq_vol_tarrif", allocationSize = 1)
    @Column(name = "id_vol_tarrif")
    private Integer idVolTarrif;
    
    @Column(name = "numero", unique = true, length = 10)
    private String numero;
    
    @Column(name = "prix", nullable = false, precision = 10, scale = 2)
    private BigDecimal prix;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "siege_categorie_id", nullable = false)
    private SiegeCategorie siegeCategorie;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vol_id", nullable = false)
    private Vol vol;
}
