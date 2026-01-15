package com.itu.compagnie_aerienne.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itu.compagnie_aerienne.model.MethodePaiement;
import com.itu.compagnie_aerienne.model.Paiement;
import com.itu.compagnie_aerienne.model.Reservation;
import com.itu.compagnie_aerienne.model.enums.StatutPaiement;
import com.itu.compagnie_aerienne.repository.PaiementRepository;
import com.itu.compagnie_aerienne.repository.ReservationRepository;
import com.itu.compagnie_aerienne.service.MethodePaiementService;
import com.itu.compagnie_aerienne.service.PaiementMethodeService;
import com.itu.compagnie_aerienne.service.PaiementService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PaymentController {

    private final ReservationRepository reservationRepository;
    private final PaiementRepository paiementRepository;
    private final PaiementService paiementService;
    private final PaiementMethodeService paiementMethodeService;
    private final MethodePaiementService methodePaiementService;

    @PostMapping("/effectuer-paiement")
    public String effectuerPaiement(@RequestParam("reservationId") Integer reservationId,
                                   @RequestParam Map<String, String> allParams,
                                   Model model) {
        try {
            // Récupérer la réservation
            Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Réservation non trouvée"));

            // Récupérer le paiement existant
            Paiement paiement = paiementRepository.findAll().stream()
                .filter(p -> p.getReservation().getId().equals(reservationId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Paiement non trouvé"));

            // Calculer le reste à payer
            BigDecimal resteAPayer = paiementService.calculerResteAPayer(paiement);

            // Extraire les méthodes de paiement et montants
            List<Integer> methodeIds = new ArrayList<>();
            List<BigDecimal> montants = new ArrayList<>();
            
            for (Map.Entry<String, String> entry : allParams.entrySet()) {
                if (entry.getKey().startsWith("methode_")) {
                    int index = Integer.parseInt(entry.getKey().substring(8));
                    methodeIds.add(Integer.parseInt(entry.getValue()));
                }
                if (entry.getKey().startsWith("montant_")) {
                    int index = Integer.parseInt(entry.getKey().substring(8));
                    montants.add(new BigDecimal(entry.getValue()));
                }
            }

            // Calculer la somme totale entrée
            BigDecimal sommeTotale = montants.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Vérifier que la somme ne dépasse pas le reste à payer
            if (sommeTotale.compareTo(resteAPayer) > 0) {
                model.addAttribute("error", "La somme des montants dépasse le reste à payer");
                return "redirect:/reservation?id=" + reservation.getVol().getId();
            }

            // Créer les paiements multiples
            List<MethodePaiement> methodesPaiement = new ArrayList<>();
            for (Integer methodeId : methodeIds) {
                MethodePaiement methode = methodePaiementService.getAllMethodesPaiement().stream()
                    .filter(m -> m.getId().equals(methodeId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Méthode de paiement non trouvée"));
                methodesPaiement.add(methode);
            }

            paiementMethodeService.creerPaiementsMultiples(paiement, methodesPaiement, montants);

            // Calculer le nouveau reste à payer
            BigDecimal nouveauReste = paiementService.calculerResteAPayer(paiement);

            // Si le paiement est complet, changer le statut à VALIDE
            if (nouveauReste.compareTo(BigDecimal.ZERO) == 0) {
                paiement.setStatut(StatutPaiement.VALIDE);
                paiementRepository.save(paiement);
            }

            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/";
        }
    }
}
