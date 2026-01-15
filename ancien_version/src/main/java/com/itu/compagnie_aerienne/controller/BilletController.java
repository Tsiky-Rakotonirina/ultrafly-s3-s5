package com.itu.compagnie_aerienne.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itu.compagnie_aerienne.model.Billet;
import com.itu.compagnie_aerienne.model.Paiement;
import com.itu.compagnie_aerienne.model.SiegeVol;
import com.itu.compagnie_aerienne.model.Vol;
import com.itu.compagnie_aerienne.model.enums.StatutPaiement;
import com.itu.compagnie_aerienne.repository.PaiementRepository;
import com.itu.compagnie_aerienne.service.BilletService;
import com.itu.compagnie_aerienne.service.ClasseSiegeService;
import com.itu.compagnie_aerienne.service.SiegeVolService;
import com.itu.compagnie_aerienne.service.VolService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BilletController {
    
    private final VolService volService;
    private final SiegeVolService siegeVolService;
    private final BilletService billetService;
    private final ClasseSiegeService classeSiegeService;
    private final PaiementRepository paiementRepository;

    @GetMapping("/billet")
    public String billet(@RequestParam("id") Integer volId,
                        @RequestParam(required = false) Integer classeSiegeId,
                        @RequestParam(required = false) Boolean occupe,
                        @RequestParam(required = false) String statutPaiement,
                        Model model) {
        
        // Récupérer le vol
        Vol vol = volService.getVolById(volId).orElse(null);
        
        // Récupérer les sièges avec filtres
        List<SiegeVol> siegesVol = siegeVolService.getSiegesByVolIdWithFilters(
            volId, classeSiegeId, occupe, statutPaiement
        );
        
        // Récupérer tous les billets pour ce vol
        List<Billet> billets = billetService.getBilletsByVolId(volId);
        
        // Créer une map pour associer siegeVol à son client et statuts
        Map<Integer, Map<String, Object>> siegeInfo = new HashMap<>();
        for (SiegeVol siegeVol : siegesVol) {
            Map<String, Object> info = new HashMap<>();
            
            // Trouver le billet correspondant
            Billet billet = billets.stream()
                .filter(b -> b.getSiegeVol().getId().equals(siegeVol.getId()))
                .findFirst()
                .orElse(null);
            
            if (billet != null) {
                info.put("client", billet.getReservation().getClient());
                info.put("statutBillet", billet.getStatut());
                info.put("prix", billet.getPrix());
                
                // Récupérer le statut de paiement
                Paiement paiement = paiementRepository.findAll().stream()
                    .filter(p -> p.getReservation().getId().equals(billet.getReservation().getId()))
                    .findFirst()
                    .orElse(null);
                
                info.put("statutPaiement", paiement != null ? paiement.getStatut() : StatutPaiement.EN_ATTENTE);
            } else {
                info.put("client", null);
                info.put("statutBillet", null);
                info.put("statutPaiement", null);
                info.put("prix", null);
            }
            
            siegeInfo.put(siegeVol.getId(), info);
        }
        
        // Filtrer par statut de paiement si spécifié
        if (statutPaiement != null && !statutPaiement.isEmpty()) {
            siegesVol = siegesVol.stream()
                .filter(sv -> {
                    Map<String, Object> info = siegeInfo.get(sv.getId());
                    StatutPaiement statut = (StatutPaiement) info.get("statutPaiement");
                    return statut != null && statut.name().equals(statutPaiement);
                })
                .toList();
        }
        
        model.addAttribute("vol", vol);
        model.addAttribute("siegesVol", siegesVol);
        model.addAttribute("siegeInfo", siegeInfo);
        model.addAttribute("classeSieges", classeSiegeService.getAllClasseSieges());
        model.addAttribute("classeSiegeId", classeSiegeId);
        model.addAttribute("occupe", occupe);
        model.addAttribute("statutPaiement", statutPaiement);
        
        return "billet";
    }
}
