package com.itu.compagnie_aerienne.controller.paiement;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itu.compagnie_aerienne.model.*;
import com.itu.compagnie_aerienne.service.PaiementSaisieService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PaiementSaisieController {
    
    private final PaiementSaisieService paiementSaisieService;
    
    /**
     * Affiche le formulaire de paiement pour une réservation
     */
    @GetMapping("/paiement-saisie")
    public String afficherFormulaire(@RequestParam("reservationId") Integer reservationId, Model model) {
        // Récupérer la réservation
        Reservation reservation = paiementSaisieService.getReservationById(reservationId)
            .orElseThrow(() -> new IllegalArgumentException("Réservation non trouvée"));
        
        // Récupérer le paiement
        Paiement paiement = paiementSaisieService.getPaiementByReservationId(reservationId)
            .orElseThrow(() -> new IllegalArgumentException("Paiement non trouvé pour cette réservation"));
        
        // Récupérer les billets
        List<ReservationBillet> billets = paiementSaisieService.getBilletsByReservationId(reservationId);
        
        // Récupérer les modes de paiement
        List<PaiementMode> modes = paiementSaisieService.getAllPaiementModes();
        
        // Récupérer les devises
        List<Devise> devises = paiementSaisieService.getAllDevises();
        
        // Récupérer les paiements déjà effectués
        List<PaiementDetail> paiementsEffectues = paiementSaisieService.getPaiementDetails(paiement.getIdPaiement());
        
        // Calculer le reste à payer
        BigDecimal resteAPayer = paiementSaisieService.calculerResteAPayer(paiement);
        
        // Vérifier si le paiement est complet
        boolean paiementComplet = paiementSaisieService.isPaiementComplet(paiement);
        
        model.addAttribute("reservation", reservation);
        model.addAttribute("paiement", paiement);
        model.addAttribute("billets", billets);
        model.addAttribute("modes", modes);
        model.addAttribute("devises", devises);
        model.addAttribute("paiementsEffectues", paiementsEffectues);
        model.addAttribute("resteAPayer", resteAPayer);
        model.addAttribute("paiementComplet", paiementComplet);
        
        return "paiement-saisie";
    }
    
    /**
     * Traite la soumission du formulaire de paiement
     */
    @PostMapping("/paiement-saisie")
    public String effectuerPaiement(
            @RequestParam("reservationId") Integer reservationId,
            @RequestParam("paiementId") Integer paiementId,
            @RequestParam("modeId") Integer modeId,
            @RequestParam("deviseId") Integer deviseId,
            @RequestParam("montant") BigDecimal montant,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Effectuer le paiement
            paiementSaisieService.effectuerPaiement(paiementId, modeId, deviseId, montant);
            
            // Vérifier si le paiement est complet
            Paiement paiement = paiementSaisieService.getPaiementByReservationId(reservationId)
                .orElseThrow();
            
            if (paiementSaisieService.isPaiementComplet(paiement)) {
                redirectAttributes.addFlashAttribute("success", "Paiement complet! Votre réservation est confirmée.");
                return "redirect:/vol-list";
            } else {
                BigDecimal reste = paiementSaisieService.calculerResteAPayer(paiement);
                redirectAttributes.addFlashAttribute("success", "Paiement partiel enregistré. Reste à payer: " + reste + " €");
                return "redirect:/paiement-saisie?reservationId=" + reservationId;
            }
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur: " + e.getMessage());
            return "redirect:/paiement-saisie?reservationId=" + reservationId;
        }
    }
}
