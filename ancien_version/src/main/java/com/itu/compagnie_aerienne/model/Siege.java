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
@Table(name = "siege", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"avion_id", "numero_siege"})
})
@Data
@NoArgsConstructor
public class Siege {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "numero_siege", nullable = false, length = 10)
    private String numeroSiege;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "classe_siege_id", nullable = false)
    private ClasseSiege classeSiege;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "avion_id", nullable = false)
    private Avion avion;
}
