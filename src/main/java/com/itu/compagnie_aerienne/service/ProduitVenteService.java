package com.itu.compagnie_aerienne.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itu.compagnie_aerienne.model.ProduitVente;
import com.itu.compagnie_aerienne.model.ProduitVenteDetail;
import com.itu.compagnie_aerienne.repository.ProduitVenteDetailRepository;
import com.itu.compagnie_aerienne.repository.ProduitVenteRepository;

@Service
public class ProduitVenteService {

    private final ProduitVenteRepository produitVenteRepository;
    private final ProduitVenteDetailRepository produitVenteDetailRepository;

    public ProduitVenteService(ProduitVenteRepository produitVenteRepository,
                               ProduitVenteDetailRepository produitVenteDetailRepository) {
        this.produitVenteRepository = produitVenteRepository;
        this.produitVenteDetailRepository = produitVenteDetailRepository;
    }

    /**
     * Calcule le chiffre d'affaires des produits vendus entre deux dates
     * @param dateMin Date de début
     * @param dateMax Date de fin
     * @return La somme de (quantité * prix_unitaire) pour tous les produits vendus
     */
    public BigDecimal calculateCaProduit(LocalDate dateMin, LocalDate dateMax) {
        BigDecimal caProduit = BigDecimal.ZERO;

        // Récupérer toutes les ventes entre les dates
        List<ProduitVente> ventes = produitVenteRepository.findByDateVenteBetween(dateMin, dateMax);

        // Pour chaque vente, calculer le CA
        for (ProduitVente vente : ventes) {
            // Récupérer les détails de la vente
            List<ProduitVenteDetail> details = produitVenteDetailRepository
                    .findByProduitVenteIdProduitVente(vente.getIdProduitVente());

            // Pour chaque détail, ajouter (quantité * prix_unitaire)
            for (ProduitVenteDetail detail : details) {
                if (detail.getProduit() != null && detail.getProduit().getPrixUnitaire() != null) {
                    BigDecimal quantite = BigDecimal.valueOf(detail.getQuantite());
                    BigDecimal prixUnitaire = detail.getProduit().getPrixUnitaire();
                    BigDecimal montant = quantite.multiply(prixUnitaire);
                    caProduit = caProduit.add(montant);
                }
            }
        }

        return caProduit;
    }
}
