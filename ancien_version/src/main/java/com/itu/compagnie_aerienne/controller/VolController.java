package com.itu.compagnie_aerienne.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itu.compagnie_aerienne.model.Vol;
import com.itu.compagnie_aerienne.service.ClasseSiegeService;
import com.itu.compagnie_aerienne.service.ClientService;
import com.itu.compagnie_aerienne.service.PrixVolService;
import com.itu.compagnie_aerienne.service.SiegeVolService;
import com.itu.compagnie_aerienne.service.VolService;

@Controller

public class VolController {
    private final VolService volService;
    private final SiegeVolService siegeVolService;
    private final ClientService clientService;
    private final ClasseSiegeService classeSiegeService;
    private final PrixVolService prixVolService;

    public VolController(VolService volService, SiegeVolService siegeVolService, 
                         ClientService clientService, ClasseSiegeService classeSiegeService,
                         PrixVolService prixVolService) {
        this.volService = volService;
        this.siegeVolService = siegeVolService;
        this.clientService = clientService;
        this.classeSiegeService = classeSiegeService;
        this.prixVolService = prixVolService;
    }

    @GetMapping
    public String vol(Model model) {
        model.addAttribute("vols", volService.getAllVols());
        return "vol";
    }

    @GetMapping("/reservation")
    public String reservation(@RequestParam("id") String idString, 
                            @RequestParam(value = "numero_siege", required = false) String numeroSiege,
                            @RequestParam(value = "classe_siege", required = false) String classeSiegeIdString,
                            Model model) {

        try {
            Integer id = Integer.parseInt(idString);
            Vol vol = volService.getVolById(id).orElse(null);
            
            if (vol != null) {
                model.addAttribute("vol", vol);
                
                // Appliquer les filtres si présents
                Integer classeSiegeId = null;
                if (classeSiegeIdString != null && !classeSiegeIdString.isEmpty()) {
                    try {
                        classeSiegeId = Integer.parseInt(classeSiegeIdString);
                    } catch (NumberFormatException e) {
                        // Ignorer si la conversion échoue
                    }
                }
                
                // Récupérer les sièges filtrés
                var siegesVol = siegeVolService.getSiegesByVolIdFiltered(id, numeroSiege, classeSiegeId);
                model.addAttribute("siegesVol", siegesVol);
                
                // Créer une map des prix pour chaque siège (siegeVolId -> prix)
                model.addAttribute("prixSiegesMap", prixVolService.getPrixMapForSiegesVol(siegesVol));
                
                model.addAttribute("prixVol", prixVolService.getAllPrixVolByVolId(id));
                model.addAttribute("clients", clientService.getAllClients());
                model.addAttribute("classeSieges", classeSiegeService.getAllClasseSieges());
                
                // Garder les valeurs du filtre dans le formulaire
                model.addAttribute("filtreNumeroSiege", numeroSiege != null ? numeroSiege : "");
                model.addAttribute("filtreClasseSiege", classeSiegeIdString != null ? classeSiegeIdString : "");
            }
        } catch (NumberFormatException e) {
            e.getMessage();
        }

        return "reservation";

    }
}
