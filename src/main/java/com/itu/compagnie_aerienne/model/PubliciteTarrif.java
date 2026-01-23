package com.itu.compagnie_aerienne.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "publicite_tarrif")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PubliciteTarrif {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_publicite_tarrif")
    private Integer idPubliciteTarrif;
    
    @Column(name = "cout", nullable = false, precision = 15, scale = 2)
    private BigDecimal cout;
    
    @Column(name = "date_tarrif", nullable = false)
    private LocalDate dateTarrif;
    
    @Column(name = "duree_min", precision = 5, scale = 2)
    private BigDecimal dureeMin;
    
    @Column(name = "duree_max", precision = 5, scale = 2)
    private BigDecimal dureeMax;
}
