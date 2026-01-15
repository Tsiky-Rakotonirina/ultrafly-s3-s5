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

import com.itu.compagnie_aerienne.model.*;
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
        
        return "reservation-saisie";
    }
    
    /**
     * Traite la soumission du formulaire de réservation
     */
    @PostMapping("/reservation-saisie")
    public String creerReservation(
            @RequestParam("volId") Integer volId,
            @RequestParam("clientId") Integer clientId,
            @RequestParam Map<String, String> allParams,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Extraire les sièges sélectionnés
            List<Integer> siegeIds = new ArrayList<>();
            for (Map.Entry<String, String> entry : allParams.entrySet()) {
                if (entry.getKey().startsWith("siege_") && "on".equals(entry.getValue())) {
                    Integer siegeId = Integer.parseInt(entry.getKey().substring(6));
                    siegeIds.add(siegeId);
                }
            }
            
            if (siegeIds.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Veuillez sélectionner au moins un siège");
                return "redirect:/reservation-saisie?volId=" + volId;
            }
            
            // Créer la réservation
            Reservation reservation = reservationSaisieService.creerReservation(volId, clientId, siegeIds);
            
            redirectAttributes.addFlashAttribute("success", "Réservation créée avec succès! Numéro: " + reservation.getNumero());
            
            // Rediriger vers la page de paiement
            return "redirect:/paiement-saisie?reservationId=" + reservation.getIdReservation();
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur: " + e.getMessage());
            return "redirect:/reservation-saisie?volId=" + volId;
        }
    }
}
