package com.itu.compagnie_aerienne.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itu.compagnie_aerienne.model.Billet;
import com.itu.compagnie_aerienne.model.Client;
import com.itu.compagnie_aerienne.model.Paiement;
import com.itu.compagnie_aerienne.model.Reservation;
import com.itu.compagnie_aerienne.model.SiegeVol;
import com.itu.compagnie_aerienne.model.Vol;
import com.itu.compagnie_aerienne.service.BilletService;
import com.itu.compagnie_aerienne.service.ClientService;
import com.itu.compagnie_aerienne.service.MethodePaiementService;
import com.itu.compagnie_aerienne.service.PaiementService;
import com.itu.compagnie_aerienne.service.ReservationService;
import com.itu.compagnie_aerienne.service.SiegeVolService;
import com.itu.compagnie_aerienne.service.VolService;

@Controller
public class ReservationController {
    private final ReservationService reservationService;
    private final VolService volService;
    private final ClientService clientService;
    private final SiegeVolService siegeVolService;
    private final BilletService billetService;
    private final MethodePaiementService methodePaiementService;
    private final PaiementService paiementService;

    public ReservationController(ReservationService reservationService,
                                VolService volService,
                                ClientService clientService,
                                SiegeVolService siegeVolService,
                                BilletService billetService,
                                MethodePaiementService methodePaiementService,
                                PaiementService paiementService) {
        this.reservationService = reservationService;
        this.volService = volService;
        this.clientService = clientService;
        this.siegeVolService = siegeVolService;
        this.billetService = billetService;
        this.methodePaiementService = methodePaiementService;
        this.paiementService = paiementService;
    }

    @PostMapping("/reserver")
    public String reserver(@RequestParam("volId") Integer volId,
                          @RequestParam("clientId") Integer clientId,
                          @RequestParam Map<String, String> allParams,
                          Model model) {
        try {
            // Récupérer le vol et le client
            Vol vol = volService.getVolById(volId).orElseThrow(() -> 
                new IllegalArgumentException("Vol non trouvé"));
            Client client = clientService.getAllClients().stream()
                .filter(c -> c.getId().equals(clientId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Client non trouvé"));

            // Récupérer les sièges sélectionnés
            List<SiegeVol> siegesSelectionnes = new ArrayList<>();
            for (Map.Entry<String, String> entry : allParams.entrySet()) {
                if (entry.getKey().startsWith("siege_") && "on".equals(entry.getValue())) {
                    Integer siegeVolId = Integer.parseInt(entry.getKey().substring(6));
                    SiegeVol siegeVol = siegeVolService.getAllSiegeVols().stream()
                        .filter(sv -> sv.getId().equals(siegeVolId))
                        .findFirst()
                        .orElse(null);
                    if (siegeVol != null) {
                        siegesSelectionnes.add(siegeVol);
                    }
                }
            }

            if (siegesSelectionnes.isEmpty()) {
                model.addAttribute("error", "Veuillez sélectionner au moins un siège");
                return "redirect:/reservation?id=" + volId;
            }

            // Créer la réservation
            reservationService.creerReservation(vol, client, siegesSelectionnes);
            
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/reservation?id=" + volId;
        }
    }

    @PostMapping("/payer")
    public String payer(@RequestParam("volId") Integer volId,
                       @RequestParam("clientId") Integer clientId,
                       @RequestParam Map<String, String> allParams,
                       Model model) {
        try {
            // Récupérer le vol et le client
            Vol vol = volService.getVolById(volId).orElseThrow(() -> 
                new IllegalArgumentException("Vol non trouvé"));
            Client client = clientService.getAllClients().stream()
                .filter(c -> c.getId().equals(clientId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Client non trouvé"));

            // Récupérer les sièges sélectionnés
            List<SiegeVol> siegesSelectionnes = new ArrayList<>();
            for (Map.Entry<String, String> entry : allParams.entrySet()) {
                if (entry.getKey().startsWith("siege_") && "on".equals(entry.getValue())) {
                    Integer siegeVolId = Integer.parseInt(entry.getKey().substring(6));
                    SiegeVol siegeVol = siegeVolService.getAllSiegeVols().stream()
                        .filter(sv -> sv.getId().equals(siegeVolId))
                        .findFirst()
                        .orElse(null);
                    if (siegeVol != null) {
                        siegesSelectionnes.add(siegeVol);
                    }
                }
            }

            if (siegesSelectionnes.isEmpty()) {
                model.addAttribute("error", "Veuillez sélectionner au moins un siège");
                return "redirect:/reservation?id=" + volId;
            }

            // Créer la réservation
            Reservation reservation = reservationService.creerReservation(vol, client, siegesSelectionnes);
            
            // Récupérer les billets créés
            List<Billet> billets = billetService.getBilletsByReservation(reservation);
            
            // Calculer le montant total
            BigDecimal montantTotal = billetService.calculerMontantTotal(billets);
            
            // Créer le paiement
            Paiement paiement = paiementService.creerPaiement(reservation, montantTotal);
            
            // Calculer le reste à payer
            BigDecimal resteAPayer = paiementService.calculerResteAPayer(paiement);

            // Préparer les attributs pour la page de paiement
            model.addAttribute("vol", vol);
            model.addAttribute("siegesReserves", siegesSelectionnes);
            model.addAttribute("billets", billets);
            model.addAttribute("montantTotal", resteAPayer);
            model.addAttribute("reservation", reservation);
            model.addAttribute("methodesPaiement", methodePaiementService.getAllMethodesPaiement());

            return "payment";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/reservation?id=" + volId;
        }
    }
}
