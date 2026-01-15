package com.itu.compagnie_aerienne.controller.reservation;

import com.itu.compagnie_aerienne.model.*;
import com.itu.compagnie_aerienne.service.ReservationService;
import com.itu.compagnie_aerienne.service.vol.VolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;
    
    @Autowired
    private VolService volService;

    // ==================== LISTE avec Filtres ====================
    
    @GetMapping
    public String liste(Model model,
                        @RequestParam(required = false) String numero,
                        @RequestParam(required = false) Integer clientId,
                        @RequestParam(required = false) Integer volId,
                        @RequestParam(required = false) Integer statutId,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        
        List<Reservation> reservations;
        
        if (numero != null || clientId != null || volId != null || statutId != null ||
            dateDebut != null || dateFin != null) {
            reservations = reservationService.filtrer(numero, clientId, volId, statutId, dateDebut, dateFin);
        } else {
            reservations = reservationService.findAllReservations();
        }
        
        model.addAttribute("reservations", reservations);
        model.addAttribute("clients", reservationService.findAllClients());
        model.addAttribute("vols", reservationService.findAllVols());
        model.addAttribute("statuts", reservationService.findAllStatuts());
        
        // Conserver les filtres
        model.addAttribute("numeroFiltre", numero);
        model.addAttribute("clientIdFiltre", clientId);
        model.addAttribute("volIdFiltre", volId);
        model.addAttribute("statutIdFiltre", statutId);
        model.addAttribute("dateDebutFiltre", dateDebut);
        model.addAttribute("dateFinFiltre", dateFin);
        
        return "reservation/reservation-liste";
    }

    // ==================== FICHE Détail ====================
    
    @GetMapping("/{id}")
    public String fiche(@PathVariable Integer id, Model model) {
        Reservation reservation = reservationService.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        
        Paiement paiement = reservationService.findPaiementByReservation(id);
        
        model.addAttribute("reservation", reservation);
        model.addAttribute("billets", reservationService.findBilletsByReservation(id));
        model.addAttribute("paiement", paiement);
        model.addAttribute("paiementDetails", paiement != null ? 
            reservationService.findPaiementDetailsByPaiement(paiement.getIdPaiement()) : null);
        model.addAttribute("historiques", reservationService.findHistoriqueByReservation(id));
        model.addAttribute("statuts", reservationService.findAllStatuts());
        model.addAttribute("devises", reservationService.findAllDevises());
        model.addAttribute("paiementModes", reservationService.findAllPaiementModes());
        
        // Calculs
        model.addAttribute("montantTotal", reservationService.getMontantTotal(id));
        model.addAttribute("totalPaye", reservationService.getTotalPaye(id));
        model.addAttribute("restePayer", reservationService.getRestePayer(id));
        
        return "reservation/reservation-fiche";
    }

    // ==================== SAISIE - Sélection Client/Vol ====================
    
    @GetMapping("/saisie")
    public String saisieForm(Model model) {
        model.addAttribute("clients", reservationService.findAllClients());
        model.addAttribute("vols", reservationService.findVolsDisponibles());
        return "reservation/reservation-saisie";
    }
    
    // ==================== SAISIE - Sélection Sièges ====================
    
    @GetMapping("/saisie/sieges")
    public String saisieSieges(@RequestParam Integer clientId,
                               @RequestParam Integer volId,
                               Model model) {
        Vol vol = volService.findById(volId)
                .orElseThrow(() -> new RuntimeException("Vol non trouvé"));
        Client client = reservationService.findAllClients().stream()
                .filter(c -> c.getIdClient().equals(clientId))
                .findFirst().orElseThrow(() -> new RuntimeException("Client non trouvé"));
        
        // Trouver l'avion du vol
        Integer avionId = null;
        List<VolDetail> details = volService.getDetailsByVol(volId);
        if (!details.isEmpty() && details.get(0).getAvion() != null) {
            avionId = details.get(0).getAvion().getIdAvion();
        }
        
        if (avionId == null) {
            throw new RuntimeException("Aucun avion assigné à ce vol");
        }
        
        List<ReservationService.SiegeInfo> sieges = reservationService.getSiegesAvecDisponibilite(volId, avionId);
        
        model.addAttribute("client", client);
        model.addAttribute("vol", vol);
        model.addAttribute("sieges", sieges);
        model.addAttribute("tarifs", volService.getTarifsByVol(volId));
        
        return "reservation/reservation-sieges-saisie";
    }
    
    @PostMapping("/saisie")
    public String saisie(@RequestParam Integer clientId,
                         @RequestParam Integer volId,
                         @RequestParam String siegeIds,
                         RedirectAttributes redirectAttributes) {
        try {
            // Parser les IDs des sièges (format: "1,2,3")
            List<Integer> siegeIdList = Arrays.stream(siegeIds.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            
            if (siegeIdList.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Veuillez sélectionner au moins un siège");
                return "redirect:/reservation/saisie/sieges?clientId=" + clientId + "&volId=" + volId;
            }
            
            Reservation reservation = reservationService.creerReservation(clientId, volId, siegeIdList);
            redirectAttributes.addFlashAttribute("success", "Réservation créée avec succès");
            return "redirect:/reservation/" + reservation.getIdReservation();
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/reservation/saisie/sieges?clientId=" + clientId + "&volId=" + volId;
        }
    }

    // ==================== PAIEMENT ====================
    
    @GetMapping("/{id}/paiement-saisie")
    public String paiementSaisieForm(@PathVariable Integer id, Model model) {
        Reservation reservation = reservationService.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        
        Paiement paiement = reservationService.findPaiementByReservation(id);
        
        model.addAttribute("reservation", reservation);
        model.addAttribute("billets", reservationService.findBilletsByReservation(id));
        model.addAttribute("paiement", paiement);
        model.addAttribute("devises", reservationService.findAllDevises());
        model.addAttribute("paiementModes", reservationService.findAllPaiementModes());
        model.addAttribute("restePayer", reservationService.getRestePayer(id));
        model.addAttribute("totalPaye", reservationService.getTotalPaye(id));
        
        return "reservation/reservation-paiement-saisie";
    }
    
    @PostMapping("/{id}/paiement")
    public String effectuerPaiement(@PathVariable Integer id,
                                    @RequestParam BigDecimal montant,
                                    @RequestParam Integer deviseId,
                                    @RequestParam Integer paiementModeId,
                                    RedirectAttributes redirectAttributes) {
        try {
            reservationService.effectuerPaiement(id, montant, deviseId, paiementModeId);
            
            BigDecimal reste = reservationService.getRestePayer(id);
            if (reste.compareTo(BigDecimal.ZERO) <= 0) {
                redirectAttributes.addFlashAttribute("success", "Paiement complet effectué avec succès");
            } else {
                redirectAttributes.addFlashAttribute("success", "Paiement partiel enregistré. Reste à payer: " + reste + " MGA");
            }
            return "redirect:/reservation/" + id;
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/reservation/" + id + "/paiement-saisie";
        }
    }

    // ==================== Changement Statut ====================
    
    @GetMapping("/{id}/statut-saisie")
    public String statutSaisieForm(@PathVariable Integer id, Model model) {
        Reservation reservation = reservationService.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        
        model.addAttribute("reservation", reservation);
        model.addAttribute("statuts", reservationService.findAllStatuts());
        return "reservation/reservation-statut-saisie";
    }
    
    @PostMapping("/{id}/statut")
    public String changerStatut(@PathVariable Integer id,
                                @RequestParam Integer statutId,
                                RedirectAttributes redirectAttributes) {
        reservationService.changerStatut(id, statutId);
        redirectAttributes.addFlashAttribute("success", "Statut de la réservation modifié avec succès");
        return "redirect:/reservation/" + id;
    }

    // ==================== Annulation ====================
    
    @PostMapping("/{id}/annuler")
    public String annulerReservation(@PathVariable Integer id,
                                     RedirectAttributes redirectAttributes) {
        try {
            reservationService.annulerReservation(id);
            redirectAttributes.addFlashAttribute("success", "Réservation annulée avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/reservation/" + id;
    }
}
