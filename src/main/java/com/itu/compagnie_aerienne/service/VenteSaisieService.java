package com.itu.compagnie_aerienne.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itu.compagnie_aerienne.model.Produit;
import com.itu.compagnie_aerienne.model.ProduitVente;
import com.itu.compagnie_aerienne.model.ProduitVenteDetail;
import com.itu.compagnie_aerienne.repository.ProduitRepository;
import com.itu.compagnie_aerienne.repository.ProduitVenteDetailRepository;
import com.itu.compagnie_aerienne.repository.ProduitVenteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VenteSaisieService {
    private final ProduitVenteRepository produitVenteRepository;
    private final ProduitRepository produitRepository;
    private final ProduitVenteDetailRepository produitVenteDetailRepository;

    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    public void saisirVenteProduit(Integer idProduit, LocalDate dateVente, Integer quantite) {
        // Créer une nouvelle vente
        ProduitVente vente = new ProduitVente();
        vente.setDateVente(dateVente);
        vente = produitVenteRepository.save(vente);

        // Récupérer le produit
        Produit produit = produitRepository.findById(idProduit)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        // Créer le détail de la vente
        ProduitVenteDetail detail = new ProduitVenteDetail();
        detail.setProduitVente(vente);
        detail.setProduit(produit);
        detail.setQuantite(quantite);
        produitVenteDetailRepository.save(detail);
    }
}
