package com.itu.compagnie_aerienne.controller.avion;

import com.itu.compagnie_aerienne.model.*;
import com.itu.compagnie_aerienne.service.avion.AvionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/avion")
public class AvionController {

    @Autowired
    private AvionService avionService;

    // ==================== LISTE avec Filtres ====================
    
    @GetMapping
    public String liste(Model model,
                        @RequestParam(required = false) String constructeur,
                        @RequestParam(required = false) String modele,
                        @RequestParam(required = false) Integer capaciteMin,
                        @RequestParam(required = false) Integer capaciteMax,
                        @RequestParam(required = false) BigDecimal consommationMin,
                        @RequestParam(required = false) BigDecimal consommationMax,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate datePossessionDebut,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate datePossessionFin,
                        @RequestParam(required = false) Integer carburantId) {
        
        List<Avion> avions;
        
        // Si au moins un filtre est renseigné, filtrer
        if (constructeur != null || modele != null || capaciteMin != null || capaciteMax != null ||
            consommationMin != null || consommationMax != null || datePossessionDebut != null ||
            datePossessionFin != null || carburantId != null) {
            avions = avionService.filtrer(constructeur, modele, capaciteMin, capaciteMax,
                    consommationMin, consommationMax, datePossessionDebut, datePossessionFin, carburantId);
        } else {
            avions = avionService.findAll();
        }
        
        model.addAttribute("avions", avions);
        model.addAttribute("carburants", avionService.getAllCarburants());
        model.addAttribute("statuts", avionService.getAllStatuts());
        
        // Conserver les valeurs des filtres
        model.addAttribute("constructeurFiltre", constructeur);
        model.addAttribute("modeleFiltre", modele);
        model.addAttribute("capaciteMinFiltre", capaciteMin);
        model.addAttribute("capaciteMaxFiltre", capaciteMax);
        model.addAttribute("consommationMinFiltre", consommationMin);
        model.addAttribute("consommationMaxFiltre", consommationMax);
        model.addAttribute("datePossessionDebutFiltre", datePossessionDebut);
        model.addAttribute("datePossessionFinFiltre", datePossessionFin);
        model.addAttribute("carburantIdFiltre", carburantId);
        
        return "avion/avion-liste";
    }

    // ==================== FICHE Détail ====================
    
    @GetMapping("/{id}")
    public String fiche(@PathVariable Integer id, Model model) {
        Avion avion = avionService.findById(id)
                .orElseThrow(() -> new RuntimeException("Avion non trouvé"));
        
        model.addAttribute("avion", avion);
        model.addAttribute("sieges", avionService.getSiegesByAvion(id));
        model.addAttribute("historiques", avionService.getHistoriqueByAvion(id));
        model.addAttribute("ravitaillements", avionService.getRavitaillementsByAvion(id));
        model.addAttribute("statutActuel", avionService.getStatutActuel(id).orElse(null));
        model.addAttribute("nbSieges", avionService.countSiegesByAvion(id));
        model.addAttribute("totalCarburant", avionService.getTotalCarburantByAvion(id));
        model.addAttribute("statuts", avionService.getAllStatuts());
        model.addAttribute("carburants", avionService.getAllCarburants());
        
        return "avion/avion-fiche";
    }

    // ==================== SAISIE (Création/Modification) ====================
    
    @GetMapping("/saisie")
    public String saisieForm(Model model) {
        model.addAttribute("avion", new Avion());
        model.addAttribute("carburants", avionService.getAllCarburants());
        model.addAttribute("siegeCategories", avionService.getAllSiegeCategories());
        model.addAttribute("isEdit", false);
        return "avion/avion-saisie";
    }
    
    @GetMapping("/saisie/{id}")
    public String saisieEditForm(@PathVariable Integer id, Model model) {
        Avion avion = avionService.findById(id)
                .orElseThrow(() -> new RuntimeException("Avion non trouvé"));
        
        model.addAttribute("avion", avion);
        model.addAttribute("carburants", avionService.getAllCarburants());
        model.addAttribute("siegeCategories", avionService.getAllSiegeCategories());
        model.addAttribute("isEdit", true);
        return "avion/avion-saisie";
    }
    
    @PostMapping("/saisie")
    public String saisie(@ModelAttribute Avion avion,
                         @RequestParam(required = false) Integer rangees,
                         @RequestParam(required = false) Integer colonnes,
                         @RequestParam(required = false) Integer siegeCategorieId,
                         @RequestParam(required = false) Integer avionStatutId,
                         RedirectAttributes redirectAttributes) {
        
        boolean isNew = avion.getIdAvion() == null;
        Avion savedAvion = avionService.save(avion);
        
        // Génération des sièges si nouveau et paramètres fournis
        if (isNew && rangees != null && colonnes != null && siegeCategorieId != null) {
            avionService.genererSieges(savedAvion, rangees, colonnes, siegeCategorieId);
        }
        
        // Création du statut initial si nouveau et statut fourni
        if (isNew && avionStatutId != null) {
            avionService.changerStatut(savedAvion.getIdAvion(), avionStatutId);
        }
        
        redirectAttributes.addFlashAttribute("success", 
            isNew ? "Avion créé avec succès" : "Avion modifié avec succès");
        
        return "redirect:/avion/" + savedAvion.getIdAvion();
    }

    // ==================== Changement Statut ====================
    
    @GetMapping("/{id}/statut-saisie")
    public String statutSaisieForm(@PathVariable Integer id, Model model) {
        Avion avion = avionService.findById(id)
                .orElseThrow(() -> new RuntimeException("Avion non trouvé"));
        
        model.addAttribute("avion", avion);
        model.addAttribute("statuts", avionService.getAllStatuts());
        model.addAttribute("statutActuel", avionService.getStatutActuel(id).orElse(null));
        return "avion/avion-statut-saisie";
    }
    
    @PostMapping("/{id}/statut")
    public String changerStatut(@PathVariable Integer id,
                                @RequestParam Integer statutId,
                                RedirectAttributes redirectAttributes) {
        avionService.changerStatut(id, statutId);
        redirectAttributes.addFlashAttribute("success", "Statut modifié avec succès");
        return "redirect:/avion/" + id;
    }

    // ==================== Ravitaillement ====================
    
    @GetMapping("/{id}/ravitaillement-saisie")
    public String ravitaillementSaisieForm(@PathVariable Integer id, Model model) {
        Avion avion = avionService.findById(id)
                .orElseThrow(() -> new RuntimeException("Avion non trouvé"));
        
        model.addAttribute("avion", avion);
        model.addAttribute("carburants", avionService.getAllCarburants());
        return "avion/avion-ravitaillement-saisie";
    }
    
    @PostMapping("/{id}/ravitaillement")
    public String ravitailler(@PathVariable Integer id,
                              @RequestParam Integer carburantId,
                              @RequestParam BigDecimal quantite,
                              RedirectAttributes redirectAttributes) {
        avionService.ravitailler(id, carburantId, quantite);
        redirectAttributes.addFlashAttribute("success", "Ravitaillement enregistré avec succès");
        return "redirect:/avion/" + id;
    }

    // ==================== Gestion des Sièges ====================
    
    @GetMapping("/{id}/siege-saisie")
    public String siegeSaisieForm(@PathVariable Integer id, Model model) {
        Avion avion = avionService.findById(id)
                .orElseThrow(() -> new RuntimeException("Avion non trouvé"));
        
        model.addAttribute("avion", avion);
        model.addAttribute("sieges", avionService.getSiegesByAvion(id));
        model.addAttribute("siegeCategories", avionService.getAllSiegeCategories());
        return "avion/avion-siege-saisie";
    }
    
    @PostMapping("/{id}/siege")
    public String ajouterSiege(@PathVariable Integer id,
                               @RequestParam String colonne,
                               @RequestParam Integer rangee,
                               @RequestParam Integer siegeCategorieId,
                               RedirectAttributes redirectAttributes) {
        Avion avion = avionService.findById(id)
                .orElseThrow(() -> new RuntimeException("Avion non trouvé"));
        SiegeCategorie categorie = avionService.getAllSiegeCategories().stream()
                .filter(c -> c.getIdSiegeCategorie().equals(siegeCategorieId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));
        
        AvionSiege siege = new AvionSiege();
        siege.setColonne(colonne);
        siege.setRangee(rangee);
        siege.setSiegeCategorie(categorie);
        siege.setAvion(avion);
        
        avionService.saveSiege(siege);
        redirectAttributes.addFlashAttribute("success", "Siège ajouté avec succès");
        return "redirect:/avion/" + id + "/siege-saisie";
    }

    // ==================== Suppression ====================
    
    @PostMapping("/{id}/supprimer")
    public String supprimer(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        avionService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Avion supprimé avec succès");
        return "redirect:/avion";
    }
}
