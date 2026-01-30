package com.itu.compagnie_aerienne.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "produit_vente_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProduitVenteDetail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produit_vente_detail")
    private Integer idProduitVenteDetail;
    
    @ManyToOne
    @JoinColumn(name = "produit_vente_id", referencedColumnName = "id_produit_vente")
    private ProduitVente produitVente;
    
    @ManyToOne
    @JoinColumn(name = "produit_id", referencedColumnName = "id_produit")
    private Produit produit;
    
    @Column(name = "quantite", nullable = false)
    private Integer quantite;
}
