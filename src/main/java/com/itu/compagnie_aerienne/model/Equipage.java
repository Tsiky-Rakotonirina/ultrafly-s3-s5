package com.itu.compagnie_aerienne.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "equipage")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Equipage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "equipage_generator")
    @SequenceGenerator(name = "equipage_generator", sequenceName = "seq_equipage", allocationSize = 1)
    @Column(name = "id_equipage")
    private Integer idEquipage;
    
    @Column(name = "numero", unique = true, length = 10)
    private String numero;
    
    @Column(name = "nom", nullable = false, length = 150)
    private String nom;
    
    @Column(name = "date_equipage")
    private LocalDate dateEquipage;
}
