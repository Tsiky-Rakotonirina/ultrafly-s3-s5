package com.itu.compagnie_aerienne.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "client")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_generator")
    @SequenceGenerator(name = "client_generator", sequenceName = "seq_client", allocationSize = 1)
    @Column(name = "id_client")
    private Integer idClient;
    
    @Column(name = "numero", unique = true, length = 10)
    private String numero;
    
    @Column(name = "passeport", length = 50)
    private String passeport;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "personne_id", nullable = false)
    private Personne personne;
}
