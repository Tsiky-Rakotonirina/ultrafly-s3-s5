package com.itu.compagnie_aerienne.controller.vol;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.itu.compagnie_aerienne.model.ClientType;
import com.itu.compagnie_aerienne.model.Paiement;
import com.itu.compagnie_aerienne.model.Reservation;
import com.itu.compagnie_aerienne.model.SiegeCategorie;
import com.itu.compagnie_aerienne.model.Vol;
import com.itu.compagnie_aerienne.model.VolDetail;
import com.itu.compagnie_aerienne.service.ReservationSaisieService;
import com.itu.compagnie_aerienne.service.VolService;

@Controller
public class VolController {

    private final VolService volService;
    private final ReservationSaisieService reservationSaisieService;

    public VolController(VolService volService, ReservationSaisieService reservationSaisieService) {
        this.volService = volService;
        this.reservationSaisieService = reservationSaisieService;
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

                // Nombre de sièges total par catégorie (référence pour toutes les catégories)
                HashMap<SiegeCategorie, BigDecimal> siegesTotal = volService
                        .getNbrAvionSiegeOrderBySiegeCategorie(avionId);
                volData.put("siegesTotal", siegesTotal);

                // Nombre de sièges pris par catégorie
                HashMap<SiegeCategorie, BigDecimal> siegesPris = volService.getSiegesPrisParCategorie(vol.getIdVol());
                // S'assurer que toutes les catégories sont présentes (avec 0 si pas de sièges
                // pris)
                HashMap<SiegeCategorie, BigDecimal> siegesPrisComplete = new HashMap<>();
                for (SiegeCategorie categorie : siegesTotal.keySet()) {
                    siegesPrisComplete.put(categorie, siegesPris.getOrDefault(categorie, BigDecimal.ZERO));
                }
                volData.put("siegesPris", siegesPrisComplete);

                // Tarif par catégorie de siège
                HashMap<SiegeCategorie, BigDecimal> tarrifs = volService.getTarrifBySiegeCategorie(vol.getIdVol());
                // S'assurer que toutes les catégories sont présentes (avec null si pas de
                // tarif)
                HashMap<SiegeCategorie, BigDecimal> tarrifsComplete = new HashMap<>();
                for (SiegeCategorie categorie : siegesTotal.keySet()) {
                    tarrifsComplete.put(categorie, tarrifs.get(categorie));
                }
                volData.put("tarrifs", tarrifsComplete);

                // Recette max par catégorie de siège
                HashMap<SiegeCategorie, BigDecimal> recetteMax = volService
                        .getRecetteMaxBySiegeCategorie(vol.getIdVol(), avionId);
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

                volsData.add(volData);
            }
        }

        model.addAttribute("volsData", volsData);

        return "vol-list";
    }

    @GetMapping("/vol-liste-reservation")   
    public String volListeReservation(Model model, Integer volId) {
        // Récupérer toutes les réservations
        List<Reservation> reservations = volService.getAllByVolIdVol(volId);
        HashMap<Reservation, Paiement> reservationPaiementMap = new HashMap<>();
        reservations.forEach(reservation -> {
            Paiement paiement = volService.getPaiementByReservationId(reservation.getIdReservation());
            reservationPaiementMap.put(reservation, paiement);
        });

        model.addAttribute("reservationPaiementMap", reservationPaiementMap);


        return "vol-liste-reservation";
    }

    @PostMapping("/simulation")
    public String simulationPost(Model model, Integer volId) {


        return "simulation";
    }

    @GetMapping("/simulation")
    public String simulationGet(Model model, Integer volId) {
        // Récupérer le vol
        Vol vol = volService.getAllVol().stream()
            .filter(v -> v.getIdVol().equals(volId))
            .findFirst()
            .orElse(null);
        
        if (vol == null) {
            return "redirect:/vol-list";
        }

        // Récupérer les détails du vol pour obtenir l'avion
        List<VolDetail> volDetails = volService.getVolDetailsByVolId(volId);
        if (volDetails.isEmpty() || volDetails.get(0).getAvion() == null) {
            return "redirect:/vol-list";
        }

        Integer avionId = volDetails.get(0).getAvion().getIdAvion();

        // Tarifs complets par catégorie de siège
        HashMap<SiegeCategorie, BigDecimal> tarrifsComplete = new HashMap<>();
        HashMap<SiegeCategorie, BigDecimal> tarrifs = volService.getTarrifBySiegeCategorie(volId);
        HashMap<SiegeCategorie, BigDecimal> siegesTotal = volService.getNbrAvionSiegeOrderBySiegeCategorie(avionId);
        for (SiegeCategorie categorie : siegesTotal.keySet()) {
            tarrifsComplete.put(categorie, tarrifs.get(categorie));
        }
        model.addAttribute("tarrifs", tarrifsComplete);

        // Créer une map JSON-compatible pour les tarifs
        Map<Integer, Map<String, Object>> tarifJsonMap = new HashMap<>();
        for (Map.Entry<SiegeCategorie, BigDecimal> entry : tarrifsComplete.entrySet()) {
            Map<String, Object> tariffData = new HashMap<>();
            tariffData.put("libelle", entry.getKey().getLibelle());
            tariffData.put("prix", entry.getValue());
            tarifJsonMap.put(entry.getKey().getIdSiegeCategorie(), tariffData);
        }
        model.addAttribute("tarifJsonMap", tarifJsonMap);

        // Tarifs avec remises complets par catégorie et type de client
        Map<String, Map<Integer, Map<String, Object>>> tarifRemises = reservationSaisieService.getTarifRemises(volId);
        model.addAttribute("tarifRemises", tarifRemises);

        // Tous les types de clients
        List<ClientType> clientTypes = reservationSaisieService.getAllClientTypes();
        model.addAttribute("clientTypes", clientTypes);

        model.addAttribute("vol", vol);
        model.addAttribute("volId", volId);

        return "simulation";
    }

}

