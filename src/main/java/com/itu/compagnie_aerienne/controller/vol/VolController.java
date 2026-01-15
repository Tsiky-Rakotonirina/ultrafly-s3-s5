package com.itu.compagnie_aerienne.controller.vol;

import com.itu.compagnie_aerienne.model.*;
import com.itu.compagnie_aerienne.service.VolService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.*;

@Controller
public class VolController {

    private final VolService volService;

    public VolController(VolService volService) {
        this.volService = volService;
    }

    @GetMapping("/vol")
    public String vol() {
        return "The features for vol are coming soon ! "; 
    }

    @GetMapping("/vol-list")
    public String volList(Model model) {
        // Récupérer tous les vols
        List<Vol> vols = volService.getAllVol();
        
        // Créer une liste pour stocker les données de chaque vol
        List<Map<String, Object>> volsData = new ArrayList<>();
        
        for (Vol vol : vols) {
            Map<String, Object> volData = new HashMap<>();
            
            // Récupérer les détails du vol pour obtenir l'avion
            List<VolDetail> volDetails = volService.getVolDetailsByVolId(vol.getIdVol());
            
            if (!volDetails.isEmpty() && volDetails.get(0).getAvion() != null) {
                Integer avionId = volDetails.get(0).getAvion().getIdAvion();
                
                // Informations de base du vol
                volData.put("vol", vol);
                
                // Nombre de sièges pris par catégorie
                HashMap<SiegeCategorie, BigDecimal> siegesPris = volService.getSiegesPrisParCategorie(vol.getIdVol());
                volData.put("siegesPris", siegesPris);
                
                // Nombre de sièges total par catégorie
                HashMap<SiegeCategorie, BigDecimal> siegesTotal = volService.getNbrAvionSiegeOrderBySiegeCategorie(avionId);
                volData.put("siegesTotal", siegesTotal);
                
                // Tarif par catégorie de siège
                HashMap<SiegeCategorie, BigDecimal> tarrifs = volService.getTarrifBySiegeCategorie(vol.getIdVol());
                volData.put("tarrifs", tarrifs);
                
                // Recette max par catégorie de siège
                HashMap<SiegeCategorie, BigDecimal> recetteMax = volService.getRecetteMaxBySiegeCategorie(vol.getIdVol(), avionId);
                volData.put("recetteMax", recetteMax);
                
                // Recette max totale
                BigDecimal recetteMaxTotal = BigDecimal.ZERO;
                for (BigDecimal recette : recetteMax.values()) {
                    recetteMaxTotal = recetteMaxTotal.add(recette);
                }
                volData.put("recetteMaxTotal", recetteMaxTotal);
                
                // Chiffre d'affaire actuel
                BigDecimal chiffreAffaire = volService.chiffreAffaire(vol.getIdVol());
                volData.put("chiffreAffaire", chiffreAffaire);
                
                volsData.add(volData);
            }
        }
        
        model.addAttribute("volsData", volsData);
        
        return "vol-list";
    }

    
    
}
