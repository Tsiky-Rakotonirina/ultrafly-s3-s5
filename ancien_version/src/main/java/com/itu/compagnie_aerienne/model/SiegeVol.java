package com.itu.compagnie_aerienne.model;

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
@Table(name = "siege_vol", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"vol_id", "siege_id"})
})
@Data
@NoArgsConstructor
public class SiegeVol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vol_id", nullable = false)
    private Vol vol;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "siege_id", nullable = false)
    private Siege siege;

    @Column(name = "occupe")
    private Boolean occupe = false;
}
