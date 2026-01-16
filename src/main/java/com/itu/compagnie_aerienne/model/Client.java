package com.itu.compagnie_aerienne.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
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
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_type_id")
    private ClientType clientType;
}
