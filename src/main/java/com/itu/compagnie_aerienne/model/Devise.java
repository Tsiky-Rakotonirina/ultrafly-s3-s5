package com.itu.compagnie_aerienne.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "devise")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Devise {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_devise")
    private Integer idDevise;
    
    @Column(name = "code", nullable = false, length = 3)
    private String code;
    
    @Column(name = "libelle", nullable = false, length = 100)
    private String libelle;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
