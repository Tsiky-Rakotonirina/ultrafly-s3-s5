package com.itu.compagnie_aerienne.controller.chiffreaffaire;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.itu.compagnie_aerienne.service.ChiffreAffaireService;

@Controller
public class ChiffreAffaireController {

    private final ChiffreAffaireService chiffreAffaireService;

    public ChiffreAffaireController(ChiffreAffaireService chiffreAffaireService) {
        this.chiffreAffaireService = chiffreAffaireService;
    }

    /**
     * Affiche la page du chiffre d'affaires avec tous les vols
     */
    @GetMapping("/chiffre-affaire")
    public String chiffreAffaire(Model model) {
        // Récupérer toutes les données de chiffre d'affaires
        List<Map<String, Object>> chiffresAffaires = chiffreAffaireService.getAllChiffreAffaireData();
        
        // Récupérer les totaux généraux
        Map<String, BigDecimal> totaux = chiffreAffaireService.getTotauxGeneraux();
        
        // Ajouter les données au modèle
        model.addAttribute("chiffresAffaires", chiffresAffaires);
        model.addAttribute("totalTickets", totaux.get("totalTickets"));
        model.addAttribute("totalPublicite", totaux.get("totalPublicite"));
        model.addAttribute("totalGeneral", totaux.get("totalGeneral"));
        
        return "chiffre-affaire";
    }
}
