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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "prix_vol", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"vol_id", "classe_siege_id"})
})
@Data
@NoArgsConstructor
public class PrixVol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vol_id", nullable = false)
    private Vol vol;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "classe_siege_id", nullable = false)
    private ClasseSiege classeSiege;

    @Column(name = "prix", nullable = false, precision = 10, scale = 2)
    private BigDecimal prix;
}
