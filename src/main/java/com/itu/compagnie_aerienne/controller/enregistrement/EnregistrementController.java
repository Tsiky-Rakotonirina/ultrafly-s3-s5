package com.itu.compagnie_aerienne.controller.enregistrement;

import com.itu.compagnie_aerienne.model.*;
import com.itu.compagnie_aerienne.service.EnregistrementService;
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
@RequestMapping("/enregistrement")
public class EnregistrementController {

    @Autowired
    private EnregistrementService enregistrementService;
    
    @Autowired
    private VolService volService;

    // ==================== LISTE avec Filtres ====================
    
    @GetMapping
    public String liste(Model model,
                        @RequestParam(required = false) String numero,
                        @RequestParam(required = false) Integer volId,
                        @RequestParam(required = false) Integer reservationId,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime heureDebut,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime heureFin,
                        @RequestParam(required = false) String status) {
        
        List<Enregistrement> enregistrements;
        
        if (numero != null || volId != null || reservationId != null || heureDebut != null || heureFin != null || status != null) {
            enregistrements = enregistrementService.filtrer(numero, volId, reservationId, heureDebut, heureFin, status);
        } else {
            enregistrements = enregistrementService.findAll();
        }
        
        model.addAttribute("enregistrements", enregistrements);
        model.addAttribute("vols", volService.findAll());
        model.addAttribute("statuts", new String[]{"Ouvert", "Clos"});
        
        // Conserver les filtres
        model.addAttribute("numeroFiltre", numero);
        model.addAttribute("volIdFiltre", volId);
        model.addAttribute("reservationIdFiltre", reservationId);
        model.addAttribute("heureDebutFiltre", heureDebut);
        model.addAttribute("heureFinFiltre", heureFin);
        model.addAttribute("statusFiltre", status);
        
        return "enregistrement/enregistrement-liste";
    }

    // ==================== FICHE Détail ====================
    
    @GetMapping("/{id}")
    public String fiche(@PathVariable Integer id, Model model) {
        Enregistrement enregistrement = enregistrementService.findById(id)
                .orElseThrow(() -> new RuntimeException("Enregistrement non trouvé"));
        
        List<EnregistrementBagage> bagages = enregistrementService.findBagagesByEnregistrement(id);
        BigDecimal totalPoids = enregistrementService.getTotalPoidsProforma(id);
        BigDecimal poidsMax = enregistrementService.getPoidsMaxAutorise(id);
        BigDecimal fraisSurpoids = enregistrementService.calculerFraisSurpoids(id);
        
        model.addAttribute("enregistrement", enregistrement);
        model.addAttribute("bagages", bagages);
        model.addAttribute("totalPoids", totalPoids);
        model.addAttribute("poidsMax", poidsMax);
        model.addAttribute("exces", enregistrementService.getExcesSurpoids(id));
        model.addAttribute("fraisSurpoids", fraisSurpoids);
        model.addAttribute("bagageTypes", enregistrementService.findAllBagageTypes());
        model.addAttribute("paiementModes", enregistrementService.findAllPaiementModes());
        model.addAttribute("devises", enregistrementService.findAllDevises());
        
        return "enregistrement/enregistrement-fiche";
    }

    // ==================== SAISIE ====================
    
    @GetMapping("/saisie")
    public String saisieForm(Model model) {
        // Liste des billets disponibles pour enregistrement
        List<ReservationBillet> billets = volService.getAllBillets();
        
        model.addAttribute("billets", billets);
        model.addAttribute("clientTypes", enregistrementService.findAllClientTypes());
        
        return "enregistrement/enregistrement-saisie";
    }
    
    @PostMapping("/saisie")
    public String saisie(@RequestParam Integer reservationBilletId,
                         @RequestParam Integer clientTypeId,
                         RedirectAttributes redirectAttributes) {
        try {
            Enregistrement enregistrement = enregistrementService.creerEnregistrement(reservationBilletId, clientTypeId);
            redirectAttributes.addFlashAttribute("success", "Enregistrement créé avec succès");
            return "redirect:/enregistrement/" + enregistrement.getIdEnregistrement();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/enregistrement/saisie";
        }
    }

    // ==================== BAGAGES ====================
    
    @GetMapping("/{id}/bagage-saisie")
    public String bagageSaisieForm(@PathVariable Integer id, Model model) {
        Enregistrement enregistrement = enregistrementService.findById(id)
                .orElseThrow(() -> new RuntimeException("Enregistrement non trouvé"));
        
        List<EnregistrementBagage> bagages = enregistrementService.findBagagesByEnregistrement(id);
        BigDecimal totalPoids = enregistrementService.getTotalPoidsProforma(id);
        BigDecimal poidsMax = enregistrementService.getPoidsMaxAutorise(id);
        
        model.addAttribute("enregistrement", enregistrement);
        model.addAttribute("bagages", bagages);
        model.addAttribute("totalPoids", totalPoids);
        model.addAttribute("poidsMax", poidsMax);
        model.addAttribute("bagageTypes", enregistrementService.findAllBagageTypes());
        
        return "enregistrement/enregistrement-bagage-saisie";
    }
    
    @PostMapping("/{id}/bagage")
    public String ajouterBagage(@PathVariable Integer id,
                                @RequestParam Integer bagageTypeId,
                                @RequestParam BigDecimal poids,
                                RedirectAttributes redirectAttributes) {
        try {
            enregistrementService.ajouterBagage(id, bagageTypeId, poids);
            redirectAttributes.addFlashAttribute("success", "Bagage ajouté avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/enregistrement/" + id + "/bagage-saisie";
    }
    
    @PostMapping("/bagage/{bagageId}/supprimer")
    public String supprimerBagage(@PathVariable Integer bagageId,
                                  @RequestParam Integer enregistrementId,
                                  RedirectAttributes redirectAttributes) {
        try {
            enregistrementService.supprimerBagage(bagageId);
            redirectAttributes.addFlashAttribute("success", "Bagage supprimé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/enregistrement/" + enregistrementId + "/bagage-saisie";
    }

    // ==================== FRAIS SURPOIDS ====================
    
    @PostMapping("/{id}/frais-surpoids")
    public String appliqueFraisSurpoids(@PathVariable Integer id,
                                        @RequestParam Integer paiementModeId,
                                        @RequestParam Integer deviseId,
                                        RedirectAttributes redirectAttributes) {
        try {
            BigDecimal exces = enregistrementService.getExcesSurpoids(id);
            if (exces.compareTo(BigDecimal.ZERO) > 0) {
                enregistrementService.appliqueFraisSurpoids(id, paiementModeId, deviseId);
                redirectAttributes.addFlashAttribute("success", "Frais de surpoids appliqués et payés");
            } else {
                redirectAttributes.addFlashAttribute("info", "Aucun surpoids détecté");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/enregistrement/" + id;
    }

    // ==================== CLÔTURE ENREGISTREMENT ====================
    
    @PostMapping("/{id}/cloturer")
    public String clorerEnregistrement(@PathVariable Integer id,
                                       RedirectAttributes redirectAttributes) {
        try {
            enregistrementService.clorerEnregistrement(id);
            enregistrementService.marquerBilletEmbarque(id);
            redirectAttributes.addFlashAttribute("success", "Enregistrement clôturé et client embarqué");
            return "redirect:/enregistrement/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/enregistrement/" + id;
        }
    }
}
