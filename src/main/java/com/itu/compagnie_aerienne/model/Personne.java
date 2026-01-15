package com.itu.compagnie_aerienne.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "personne")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Personne {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personne_generator")
    @SequenceGenerator(name = "personne_generator", sequenceName = "seq_personne", allocationSize = 1)
    @Column(name = "id_personne")
    private Integer idPersonne;
    
    @Column(name = "numero", unique = true, length = 10)
    private String numero;
    
    @Column(name = "nom", nullable = false, length = 150)
    private String nom;
    
    @Column(name = "email", length = 150)
    private String email;
    
    @Column(name = "date_naissance")
    private LocalDate dateNaissance;
    
    @Column(name = "date_personne")
    private LocalDate datePersonne;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pays_id")
    private Pays pays;
}
