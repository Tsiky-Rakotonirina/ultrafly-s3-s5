package com.itu.compagnie_aerienne.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "enregistrement_bagage")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnregistrementBagage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_enregistrement_bagage")
    private Integer idEnregistrementBagage;
    
    @Column(name = "numero", unique = true, length = 10)
    private String numero;
    
    @Column(name = "poids", nullable = false, precision = 10, scale = 2)
    private BigDecimal poids;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bagage_type_id", nullable = false)
    private BagageType bagageType;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "enregistrement_id", nullable = false)
    private Enregistrement enregistrement;
}
