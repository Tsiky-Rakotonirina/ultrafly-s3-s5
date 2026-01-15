package com.itu.compagnie_aerienne.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "equipage_membre")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipageMembre {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "equipage_membre_generator")
    @SequenceGenerator(name = "equipage_membre_generator", sequenceName = "seq_equipage_membre", allocationSize = 1)
    @Column(name = "id_equipage_membre")
    private Integer idEquipageMembre;
    
    @Column(name = "numero", unique = true, length = 10)
    private String numero;
    
    @Column(name = "ordre", nullable = false)
    private Integer ordre;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "equipage_id", nullable = false)
    private Equipage equipage;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employe_id", nullable = false)
    private Employe employe;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
