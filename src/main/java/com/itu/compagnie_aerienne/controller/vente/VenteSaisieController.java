package com.itu.compagnie_aerienne.controller.vente;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.itu.compagnie_aerienne.model.Produit;
import com.itu.compagnie_aerienne.service.VenteSaisieService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class VenteSaisieController {
    private final VenteSaisieService venteSaisieService;

    @GetMapping("/produit-vente-saisie")
    public String getAllProduits(Model model) {
        List<Produit> produits = venteSaisieService.getAllProduits();
        model.addAttribute("produits", produits);
        return "produit-vente-saisie";
    }

    @PostMapping("/produit-vente")
    public String saisirVenteProduit(Integer produitId, String dateVente, Integer quantite, Model model) {
        venteSaisieService.saisirVenteProduit(produitId, java.time.LocalDate.parse(dateVente), quantite);
        return "redirect:/produit-vente-saisie";
    }
}
