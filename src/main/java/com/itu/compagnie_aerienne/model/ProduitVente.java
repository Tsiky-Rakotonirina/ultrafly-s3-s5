package com.itu.compagnie_aerienne.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "produit_vente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProduitVente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produit_vente")
    private Integer idProduitVente;
    
    @Column(name = "date_vente")
    private LocalDate dateVente;
}
