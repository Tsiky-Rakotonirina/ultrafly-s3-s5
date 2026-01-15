package com.itu.compagnie_aerienne.controller.vol;

import com.itu.compagnie_aerienne.model.*;
import com.itu.compagnie_aerienne.service.VolService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
                Avion avion = volDetails.get(0).getAvion();
                Integer avionId = avion.getIdAvion();
                
                // Informations de base du vol
                volData.put("vol", vol);
                
                // Ajouter l'avion ID et nom pour le filtrage
                volData.put("avionId", avionId);
                String avionNom = avion.getNumero() != null ? avion.getNumero() : "";
                if (avion.getModele() != null) {
                    avionNom = avionNom.isEmpty() ? avion.getModele() : avionNom + " - " + avion.getModele();
                }
                if (avionNom.isEmpty()) {
                    avionNom = "Avion " + avionId;
                }
                volData.put("avionNom", avionNom);
                
                // Nombre de sièges total par catégorie (référence pour toutes les catégories)
                HashMap<SiegeCategorie, BigDecimal> siegesTotal = volService.getNbrAvionSiegeOrderBySiegeCategorie(avionId);
                volData.put("siegesTotal", siegesTotal);
                
                // Nombre de sièges pris par catégorie
                HashMap<SiegeCategorie, BigDecimal> siegesPris = volService.getSiegesPrisParCategorie(vol.getIdVol());
                // S'assurer que toutes les catégories sont présentes (avec 0 si pas de sièges pris)
                HashMap<SiegeCategorie, BigDecimal> siegesPrisComplete = new HashMap<>();
                for (SiegeCategorie categorie : siegesTotal.keySet()) {
                    siegesPrisComplete.put(categorie, siegesPris.getOrDefault(categorie, BigDecimal.ZERO));
                }
                volData.put("siegesPris", siegesPrisComplete);
                
                // Tarif par catégorie de siège
                HashMap<SiegeCategorie, BigDecimal> tarrifs = volService.getTarrifBySiegeCategorie(vol.getIdVol());
                // S'assurer que toutes les catégories sont présentes (avec null si pas de tarif)
                HashMap<SiegeCategorie, BigDecimal> tarrifsComplete = new HashMap<>();
                for (SiegeCategorie categorie : siegesTotal.keySet()) {
                    tarrifsComplete.put(categorie, tarrifs.get(categorie));
                }
                volData.put("tarrifs", tarrifsComplete);
                
                // Recette max par catégorie de siège
                HashMap<SiegeCategorie, BigDecimal> recetteMax = volService.getRecetteMaxBySiegeCategorie(vol.getIdVol(), avionId);
                volData.put("recetteMax", recetteMax);
                
                // Recette max totale
                BigDecimal recetteMaxTotal = BigDecimal.ZERO;
                for (BigDecimal recette : recetteMax.values()) {
                    if (recette != null) {
                        recetteMaxTotal = recetteMaxTotal.add(recette);
                    }
                }
                volData.put("recetteMaxTotal", recetteMaxTotal);
                
                // Chiffre d'affaire actuel
                BigDecimal chiffreAffaire = volService.chiffreAffaire(vol.getIdVol());
                volData.put("chiffreAffaire", chiffreAffaire);
                
                // === NOUVELLES DONNÉES POUR LE FILTRAGE ===
                
                // Nombre d'escales
                int nombreEscales = volService.countEscalesByVolId(vol.getIdVol());
                volData.put("nombreEscales", nombreEscales);
                
                // Nombre de réservations
                int nombreReservations = volService.countReservationsByVolId(vol.getIdVol());
                volData.put("nombreReservations", nombreReservations);
                
                // Tarif moyen
                BigDecimal tarrifMoyen = BigDecimal.ZERO;
                int tarrifCount = 0;
                for (BigDecimal t : tarrifsComplete.values()) {
                    if (t != null) {
                        tarrifMoyen = tarrifMoyen.add(t);
                        tarrifCount++;
                    }
                }
                if (tarrifCount > 0) {
                    tarrifMoyen = tarrifMoyen.divide(BigDecimal.valueOf(tarrifCount), 2, RoundingMode.HALF_UP);
                }
                volData.put("tarrifMoyen", tarrifMoyen);
                
                // Données JSON pour les catégories de sièges (pour le filtrage JS)
                List<Map<String, Object>> categoriesDataList = new ArrayList<>();
                for (SiegeCategorie categorie : siegesTotal.keySet()) {
                    Map<String, Object> catData = new HashMap<>();
                    catData.put("categorieId", categorie.getIdSiegeCategorie());
                    catData.put("categorieLibelle", categorie.getLibelle());
                    catData.put("siegesPris", siegesPrisComplete.getOrDefault(categorie, BigDecimal.ZERO));
                    catData.put("siegesTotal", siegesTotal.get(categorie));
                    catData.put("prix", tarrifsComplete.get(categorie) != null ? tarrifsComplete.get(categorie) : BigDecimal.ZERO);
                    catData.put("recetteMax", recetteMax.getOrDefault(categorie, BigDecimal.ZERO));
                    categoriesDataList.add(catData);
                }
                
                // Convertir en JSON string pour le data attribute
                StringBuilder jsonBuilder = new StringBuilder("[");
                for (int i = 0; i < categoriesDataList.size(); i++) {
                    Map<String, Object> cat = categoriesDataList.get(i);
                    jsonBuilder.append("{");
                    jsonBuilder.append("\"categorieId\":").append(cat.get("categorieId")).append(",");
                    jsonBuilder.append("\"categorieLibelle\":\"").append(cat.get("categorieLibelle")).append("\",");
                    jsonBuilder.append("\"siegesPris\":").append(cat.get("siegesPris")).append(",");
                    jsonBuilder.append("\"siegesTotal\":").append(cat.get("siegesTotal")).append(",");
                    jsonBuilder.append("\"prix\":").append(cat.get("prix")).append(",");
                    jsonBuilder.append("\"recetteMax\":").append(cat.get("recetteMax"));
                    jsonBuilder.append("}");
                    if (i < categoriesDataList.size() - 1) {
                        jsonBuilder.append(",");
                    }
                }
                jsonBuilder.append("]");
                volData.put("categoriesDataJson", jsonBuilder.toString());
                
                volsData.add(volData);
            }
        }
        
        model.addAttribute("volsData", volsData);
        
        return "vol-list";
    }

    
    
}
