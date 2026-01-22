package com.itu.compagnie_aerienne.controller.dashboard;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itu.compagnie_aerienne.service.DiffusionService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DiffusionController {

    private final DiffusionService diffusionService;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "redirect:/diffusion-ca";
    }

    @GetMapping("/diffusion-ca")
    public String diffusionCa(@RequestParam(name = "moisAnnee", required = false) String moisAnnee, Model model) {
        LocalDate moisAnneeDate = null;
        if (moisAnnee != null && !moisAnnee.isEmpty()) {
            // Format attendu: YYYY-MM (depuis input type="month")
            moisAnneeDate = LocalDate.parse(moisAnnee + "-01");
        }

        DiffusionService.DiffusionStats stats = diffusionService.calculerStatsDiffusion(moisAnneeDate);

        model.addAttribute("ca", stats.getChiffreAffaireTotal());
        model.addAttribute("nombreDiffusion", stats.getNombreDiffusionTotal());
        model.addAttribute("detailsParSociete", stats.getDetailsParSociete());
        model.addAttribute("moisAnnee", moisAnnee);
        model.addAttribute("societes", diffusionService.getAllSocietes());

        return "diffusion";
    }

    @PostMapping("/diffusion-saisie")
    public String diffusionSaisie(@RequestParam(name = "nombre", required = false) String nombre,
            @RequestParam(name = "moisAnnee", required = false) String moisAnnee,
            @RequestParam(name = "duree", required = false) String duree,
            @RequestParam(name = "societe", required = false) String societe) {
        Integer nombreInt = (nombre != null && !nombre.isEmpty()) ? Integer.parseInt(nombre) : null;
        LocalDate moisAnneeDate = null;
        if (moisAnnee != null && !moisAnnee.isEmpty()) {
            // Format attendu: YYYY-MM (depuis input type="month")
            moisAnneeDate = LocalDate.parse(moisAnnee + "-01");
        }
        BigDecimal dureeBigDecimal = (duree != null && !duree.isEmpty()) ? new BigDecimal(duree) : null;
        Integer societeId = (societe != null && !societe.isEmpty()) ? Integer.parseInt(societe) : null;

        String result = diffusionService.create(nombreInt, moisAnneeDate, dureeBigDecimal, societeId);
        System.out.println("Diffusion created with ID: " + result);

        return "redirect:/diffusion-ca";
    }

}
