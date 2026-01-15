package com.itu.compagnie_aerienne.controller.vol;

import com.itu.compagnie_aerienne.model.*;
import com.itu.compagnie_aerienne.service.vol.VolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/vol")
public class VolController {

    @Autowired
    private VolService volService;

    // ==================== LISTE avec Filtres ====================
    
    @GetMapping
    public String liste(Model model,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime heureDebut,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime heureFin,
                        @RequestParam(required = false) Integer volTypeId,
                        @RequestParam(required = false) Integer statutVolId,
                        @RequestParam(required = false) Integer itineraireId,
                        @RequestParam(required = false) Integer aeroportDepartId,
                        @RequestParam(required = false) Integer aeroportArriveeId) {
        
        List<Vol> vols;
        
        if (heureDebut != null || heureFin != null || volTypeId != null || statutVolId != null ||
            itineraireId != null || aeroportDepartId != null || aeroportArriveeId != null) {
            vols = volService.filtrer(heureDebut, heureFin, volTypeId, statutVolId,
                    itineraireId, aeroportDepartId, aeroportArriveeId);
        } else {
            vols = volService.findAll();
        }
        
        model.addAttribute("vols", vols);
        model.addAttribute("volTypes", volService.getAllVolTypes());
        model.addAttribute("statuts", volService.getAllStatuts());
        model.addAttribute("itineraires", volService.getAllItineraires());
        model.addAttribute("aeroports", volService.getAllAeroports());
        
        // Conserver les filtres
        model.addAttribute("heureDebutFiltre", heureDebut);
        model.addAttribute("heureFinFiltre", heureFin);
        model.addAttribute("volTypeIdFiltre", volTypeId);
        model.addAttribute("statutVolIdFiltre", statutVolId);
        model.addAttribute("itineraireIdFiltre", itineraireId);
        model.addAttribute("aeroportDepartIdFiltre", aeroportDepartId);
        model.addAttribute("aeroportArriveeIdFiltre", aeroportArriveeId);
        
        return "vol/vol-liste";
    }

    // ==================== FICHE Détail ====================
    
    @GetMapping("/{id}")
    public String fiche(@PathVariable Integer id, Model model) {
        Vol vol = volService.findById(id)
                .orElseThrow(() -> new RuntimeException("Vol non trouvé"));
        
        model.addAttribute("vol", vol);
        model.addAttribute("details", volService.getDetailsByVol(id));
        model.addAttribute("tarifs", volService.getTarifsByVol(id));
        model.addAttribute("historiques", volService.getHistoriqueByVol(id));
        model.addAttribute("reports", volService.getReportsByVol(id));
        model.addAttribute("arrets", volService.getArretsByVol(id));
        model.addAttribute("escales", volService.getEscalesByItineraire(vol.getItineraire().getIdItineraire()));
        model.addAttribute("statuts", volService.getAllStatuts());
        model.addAttribute("reportTypes", volService.getAllReportTypes());
        model.addAttribute("aeroports", volService.getAllAeroports());
        
        return "vol/vol-fiche";
    }

    // ==================== SAISIE ====================
    
    @GetMapping("/saisie")
    public String saisieForm(Model model) {
        model.addAttribute("vol", new Vol());
        model.addAttribute("volTypes", volService.getAllVolTypes());
        model.addAttribute("statuts", volService.getAllStatuts());
        model.addAttribute("itineraires", volService.getAllItineraires());
        model.addAttribute("avions", volService.getAllAvions());
        model.addAttribute("equipages", volService.getAllEquipages());
        model.addAttribute("siegeCategories", volService.getAllSiegeCategories());
        model.addAttribute("isEdit", false);
        return "vol/vol-saisie";
    }
    
    @GetMapping("/saisie/{id}")
    public String saisieEditForm(@PathVariable Integer id, Model model) {
        Vol vol = volService.findById(id)
                .orElseThrow(() -> new RuntimeException("Vol non trouvé"));
        
        model.addAttribute("vol", vol);
        model.addAttribute("volTypes", volService.getAllVolTypes());
        model.addAttribute("statuts", volService.getAllStatuts());
        model.addAttribute("itineraires", volService.getAllItineraires());
        model.addAttribute("avions", volService.getAllAvions());
        model.addAttribute("equipages", volService.getAllEquipages());
        model.addAttribute("siegeCategories", volService.getAllSiegeCategories());
        model.addAttribute("tarifs", volService.getTarifsByVol(id));
        model.addAttribute("isEdit", true);
        return "vol/vol-saisie";
    }
    
    @PostMapping("/saisie")
    public String saisie(@ModelAttribute Vol vol,
                         RedirectAttributes redirectAttributes) {
        boolean isNew = vol.getIdVol() == null;
        Vol savedVol = volService.save(vol);
        
        if (isNew) {
            // Créer l'historique initial
            volService.changerStatut(savedVol.getIdVol(), vol.getStatutVol().getIdVolStatut());
        }
        
        redirectAttributes.addFlashAttribute("success", 
            isNew ? "Vol créé avec succès" : "Vol modifié avec succès");
        
        return "redirect:/vol/" + savedVol.getIdVol();
    }

    // ==================== Changement Statut ====================
    
    @GetMapping("/{id}/statut-saisie")
    public String statutSaisieForm(@PathVariable Integer id, Model model) {
        Vol vol = volService.findById(id)
                .orElseThrow(() -> new RuntimeException("Vol non trouvé"));
        
        model.addAttribute("vol", vol);
        model.addAttribute("statuts", volService.getAllStatuts());
        return "vol/vol-statut-saisie";
    }
    
    @PostMapping("/{id}/statut")
    public String changerStatut(@PathVariable Integer id,
                                @RequestParam Integer statutId,
                                RedirectAttributes redirectAttributes) {
        volService.changerStatut(id, statutId);
        redirectAttributes.addFlashAttribute("success", "Statut du vol modifié avec succès");
        return "redirect:/vol/" + id;
    }

    // ==================== Report de Vol ====================
    
    @GetMapping("/{id}/report-saisie")
    public String reportSaisieForm(@PathVariable Integer id, Model model) {
        Vol vol = volService.findById(id)
                .orElseThrow(() -> new RuntimeException("Vol non trouvé"));
        
        model.addAttribute("vol", vol);
        model.addAttribute("details", volService.getDetailsByVol(id));
        model.addAttribute("reportTypes", volService.getAllReportTypes());
        return "vol/vol-report-saisie";
    }
    
    @PostMapping("/{id}/report")
    public String reporterVol(@PathVariable Integer id,
                              @RequestParam Integer volDetailId,
                              @RequestParam Integer reportTypeId,
                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime nouvelleHeure,
                              RedirectAttributes redirectAttributes) {
        volService.reporterVol(volDetailId, reportTypeId, nouvelleHeure);
        redirectAttributes.addFlashAttribute("success", "Vol reporté avec succès");
        return "redirect:/vol/" + id;
    }

    // ==================== Arrêt de Vol ====================
    
    @GetMapping("/{id}/arret-saisie")
    public String arretSaisieForm(@PathVariable Integer id, Model model) {
        Vol vol = volService.findById(id)
                .orElseThrow(() -> new RuntimeException("Vol non trouvé"));
        
        model.addAttribute("vol", vol);
        model.addAttribute("aeroports", volService.getAllAeroports());
        return "vol/vol-arret-saisie";
    }
    
    @PostMapping("/{id}/arret")
    public String arreterVol(@PathVariable Integer id,
                             @RequestParam Integer aeroportId,
                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime heure,
                             RedirectAttributes redirectAttributes) {
        volService.arreterVol(id, aeroportId, heure);
        redirectAttributes.addFlashAttribute("success", "Arrêt enregistré avec succès");
        return "redirect:/vol/" + id;
    }

    // ==================== Annulation ====================
    
    @PostMapping("/{id}/annuler")
    public String annulerVol(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        volService.annulerVol(id);
        redirectAttributes.addFlashAttribute("success", "Vol annulé avec succès");
        return "redirect:/vol/" + id;
    }

    // ==================== Tarifs ====================
    
    @GetMapping("/{id}/tarif-saisie")
    public String tarifSaisieForm(@PathVariable Integer id, Model model) {
        Vol vol = volService.findById(id)
                .orElseThrow(() -> new RuntimeException("Vol non trouvé"));
        
        model.addAttribute("vol", vol);
        model.addAttribute("tarifs", volService.getTarifsByVol(id));
        model.addAttribute("siegeCategories", volService.getAllSiegeCategories());
        return "vol/vol-tarif-saisie";
    }
    
    @PostMapping("/{id}/tarif")
    public String ajouterTarif(@PathVariable Integer id,
                               @RequestParam Integer siegeCategorieId,
                               @RequestParam BigDecimal prix,
                               RedirectAttributes redirectAttributes) {
        Vol vol = volService.findById(id)
                .orElseThrow(() -> new RuntimeException("Vol non trouvé"));
        SiegeCategorie categorie = volService.getAllSiegeCategories().stream()
                .filter(c -> c.getIdSiegeCategorie().equals(siegeCategorieId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));
        
        VolTarrif tarif = new VolTarrif();
        tarif.setVol(vol);
        tarif.setSiegeCategorie(categorie);
        tarif.setPrix(prix);
        
        volService.saveTarif(tarif);
        redirectAttributes.addFlashAttribute("success", "Tarif ajouté avec succès");
        return "redirect:/vol/" + id + "/tarif-saisie";
    }

    // ==================== Suppression ====================
    
    @PostMapping("/{id}/supprimer")
    public String supprimer(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        volService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Vol supprimé avec succès");
        return "redirect:/vol";
    }
}
