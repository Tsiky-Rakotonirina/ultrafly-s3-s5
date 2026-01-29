package com.itu.compagnie_aerienne.controller.dashboard;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itu.compagnie_aerienne.model.EncaissementDetail;
import com.itu.compagnie_aerienne.model.Societe;
import com.itu.compagnie_aerienne.service.EncaissementService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class EncaissementController {
    
    private final EncaissementService encaissementService;
    
    @GetMapping("/encaissement-saisie")
    public String encaissementSaisie(Model model, @RequestParam(required = false) Integer societeId) {
        // Récupérer toutes les sociétés
        List<Societe> societes = encaissementService.getAllSocietes();
        model.addAttribute("societes", societes);
        
        // Si une société est sélectionnée, récupérer les infos d'encaissement
        if (societeId != null) {
            Map<String, Object> encaissementInfo = encaissementService.getEncaissementInfoSociete(societeId);
            model.addAttribute("encaissementInfo", encaissementInfo);
            model.addAttribute("selectedSocieteId", societeId);
        }
        
        model.addAttribute("dateJour", LocalDate.now());
        
        return "encaissement-saisie";
    }
    
    @GetMapping("/api/encaissement-info")
    @ResponseBody
    public Map<String, Object> getEncaissementInfo(@RequestParam Integer societeId) {
        return encaissementService.getEncaissementInfoSociete(societeId);
    }
    
    @PostMapping("/encaissement-saisie")
    public String effectuerEncaissement(
            @RequestParam Integer societeId,
            @RequestParam BigDecimal montant,
            @RequestParam String dateEncaissement,
            Model model) {
        
        try {
            LocalDate date = LocalDate.parse(dateEncaissement);
            List<EncaissementDetail> details = encaissementService.effectuerEncaissement(societeId, montant, date);
            
            model.addAttribute("success", "Encaissement effectué avec succès ! " + details.size() + " publicité(s) encaissée(s).");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        
        // Recharger la page avec les données mises à jour
        List<Societe> societes = encaissementService.getAllSocietes();
        model.addAttribute("societes", societes);
        
        if (societeId != null) {
            Map<String, Object> encaissementInfo = encaissementService.getEncaissementInfoSociete(societeId);
            model.addAttribute("encaissementInfo", encaissementInfo);
            model.addAttribute("selectedSocieteId", societeId);
        }
        
        model.addAttribute("dateJour", LocalDate.now());
        
        return "encaissement-saisie";
    }
}
