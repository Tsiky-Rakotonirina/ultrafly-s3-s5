package com.itu.compagnie_aerienne.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employe")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employe {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employe_generator")
    @SequenceGenerator(name = "employe_generator", sequenceName = "seq_employe", allocationSize = 1)
    @Column(name = "id_employe")
    private Integer idEmploye;
    
    @Column(name = "numero", unique = true, length = 10)
    private String numero;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "poste_id", nullable = false)
    private Poste poste;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "personne_id", nullable = false)
    private Personne personne;
}
