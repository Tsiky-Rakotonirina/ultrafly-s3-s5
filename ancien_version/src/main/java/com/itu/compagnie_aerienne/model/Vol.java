package com.itu.compagnie_aerienne.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.itu.compagnie_aerienne.model.enums.StatusVol;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vol")
@Data
@NoArgsConstructor
public class Vol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "numero_vol", nullable = false, unique = true, length = 50)
    private String numeroVol;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "aeroport_depart_id", nullable = false)
    private Aeroport aeroportDepart;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "aeroport_arrivee_id", nullable = false)
    private Aeroport aeroportArrivee;

    @Column(name = "date_heure_depart", nullable = false)
    private LocalDateTime dateHeureDepart;

    @Column(name = "date_heure_arrivee", nullable = false)
    private LocalDateTime dateHeureArrivee;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "avion_id", nullable = false)
    private Avion avion;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status")
    private StatusVol status = StatusVol.PLANIFIE;
}
