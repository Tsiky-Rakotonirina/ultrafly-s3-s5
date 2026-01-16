package com.itu.compagnie_aerienne.controller.reservation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itu.compagnie_aerienne.model.Avion;
import com.itu.compagnie_aerienne.model.AvionSiege;
import com.itu.compagnie_aerienne.model.Client;
import com.itu.compagnie_aerienne.model.Reservation;
import com.itu.compagnie_aerienne.model.SiegeCategorie;
import com.itu.compagnie_aerienne.model.Vol;
import com.itu.compagnie_aerienne.service.ReservationSaisieService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReservationSaisieController {
    
    private final ReservationSaisieService reservationSaisieService;
    
    /**
     * Affiche le formulaire de réservation pour un vol
     */
    @GetMapping("/reservation-saisie")
    public String afficherFormulaire(@RequestParam("volId") Integer volId, Model model) {
        // Récupérer le vol
        Vol vol = reservationSaisieService.getVolById(volId)
            .orElseThrow(() -> new IllegalArgumentException("Vol non trouvé"));
        
        // Récupérer l'avion du vol
        Avion avion = reservationSaisieService.getAvionByVolId(volId);
        if (avion == null) {
            model.addAttribute("error", "Aucun avion assigné à ce vol");
            return "reservation-saisie";
        }
        
        // Récupérer tous les sièges de l'avion
        List<AvionSiege> sieges = reservationSaisieService.getSiegesByAvionId(avion.getIdAvion());
        
        // Récupérer les sièges déjà réservés
        List<Integer> siegesReservesIds = reservationSaisieService.getSiegesReservesIds(volId);
        
        // Récupérer les tarifs par catégorie
        Map<Integer, BigDecimal> tarifs = reservationSaisieService.getTarifsByCategorie(volId);
        
        // Récupérer les remises pour les tarifs (vol_tarrif_remise)
        Map<String, Object> tarifRemises = reservationSaisieService.getTarifRemises(volId);
        
        // Récupérer tous les clients
        List<Client> clients = reservationSaisieService.getAllClients();
        
        // Grouper les sièges par catégorie pour l'affichage
        Map<SiegeCategorie, List<Map<String, Object>>> siegesParCategorie = new HashMap<>();
        for (AvionSiege siege : sieges) {
            SiegeCategorie categorie = siege.getSiegeCategorie();
            siegesParCategorie.putIfAbsent(categorie, new ArrayList<>());
            
            Map<String, Object> siegeInfo = new HashMap<>();
            siegeInfo.put("siege", siege);
            siegeInfo.put("reserve", siegesReservesIds.contains(siege.getIdAvionSiege()));
            siegeInfo.put("prix", tarifs.getOrDefault(categorie.getIdSiegeCategorie(), BigDecimal.ZERO));
            
            siegesParCategorie.get(categorie).add(siegeInfo);
        }
        
        model.addAttribute("vol", vol);
        model.addAttribute("avion", avion);
        model.addAttribute("siegesParCategorie", siegesParCategorie);
        model.addAttribute("clients", clients);
        model.addAttribute("tarifs", tarifs);
        model.addAttribute("tarifRemises", tarifRemises);
        
        return "reservation-saisie";
    }
    
    /**
     * Traite la soumission du formulaire de réservation
     */
    @PostMapping("/reservation-saisie")
    public String creerReservation(
            @RequestParam("volId") Integer volId,
            @RequestParam("clientId") Integer clientPrincipalId,
            @RequestParam Map<String, String> allParams,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Extraire les sièges sélectionnés avec leurs clients et prix
            Map<Integer, Map<String, Object>> siegesData = new HashMap<>();
            
            for (Map.Entry<String, String> entry : allParams.entrySet()) {
                String key = entry.getKey();
                
                // Format: billet_{siegeId}_clientId ou billet_{siegeId}_prix
                if (key.startsWith("billet_")) {
                    String[] parts = key.split("_");
                    if (parts.length == 3) {
                        Integer siegeId = Integer.parseInt(parts[1]);
                        String field = parts[2]; // "clientId" ou "prix"
                        
                        siegesData.putIfAbsent(siegeId, new HashMap<>());
                        
                        if ("clientId".equals(field)) {
                            siegesData.get(siegeId).put("clientId", Integer.parseInt(entry.getValue()));
                        } else if ("prix".equals(field)) {
                            siegesData.get(siegeId).put("prix", new BigDecimal(entry.getValue()));
                        }
                    }
                }
            }
            
            if (siegesData.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Veuillez sélectionner au moins un siège");
                return "redirect:/reservation-saisie?volId=" + volId;
            }
            
            // Créer la réservation avec les nouvelles données
            Reservation reservation = reservationSaisieService.creerReservationAvecClients(volId, clientPrincipalId, siegesData);
            
            redirectAttributes.addFlashAttribute("success", "Réservation créée avec succès! Numéro: " + reservation.getNumero());
            
            // Rediriger vers la page de paiement
            return "redirect:/paiement-saisie?reservationId=" + reservation.getIdReservation();
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur: " + e.getMessage());
            return "redirect:/reservation-saisie?volId=" + volId;
        }
    }
}
