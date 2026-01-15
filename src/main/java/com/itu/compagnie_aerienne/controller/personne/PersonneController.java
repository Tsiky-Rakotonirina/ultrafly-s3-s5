package com.itu.compagnie_aerienne.controller.personne;

import com.itu.compagnie_aerienne.model.*;
import com.itu.compagnie_aerienne.service.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Controller
@RequestMapping("/personne")
public class PersonneController {

    @Autowired
    private PersonneService personneService;
    
    // ==================== MENU PRINCIPAL PERSONNE ====================
    
    @GetMapping
    public String index(Model model) {
        model.addAttribute("clients", personneService.findAllClients());
        model.addAttribute("employes", personneService.findAllEmployes());
        model.addAttribute("equipages", personneService.findAllEquipages());
        return "personne/personne-index";
    }
    
    // ==================== CLIENT ====================
    
    @GetMapping("/client")
    public String clientListe(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String passeport,
            @RequestParam(required = false) Integer paysId,
            Model model) {
        
        if (nom != null || passeport != null || paysId != null) {
            model.addAttribute("clients", personneService.filtrerClients(nom, passeport, paysId));
        } else {
            model.addAttribute("clients", personneService.findAllClients());
        }
        
        model.addAttribute("pays", personneService.findAllPays());
        model.addAttribute("nomFiltre", nom);
        model.addAttribute("passeportFiltre", passeport);
        model.addAttribute("paysIdFiltre", paysId);
        
        return "personne/client-liste";
    }
    
    @GetMapping("/client/{id}")
    public String clientFiche(@PathVariable Integer id, Model model) {
        Client client = personneService.findClientById(id)
            .orElseThrow(() -> new RuntimeException("Client non trouvé"));
        
        model.addAttribute("client", client);
        return "personne/client-fiche";
    }
    
    @GetMapping("/client/saisie")
    public String clientSaisieForm(Model model) {
        model.addAttribute("pays", personneService.findAllPays());
        return "personne/client-saisie";
    }
    
    @GetMapping("/client/saisie/{id}")
    public String clientSaisieEditForm(@PathVariable Integer id, Model model) {
        Client client = personneService.findClientById(id)
            .orElseThrow(() -> new RuntimeException("Client non trouvé"));
        
        model.addAttribute("client", client);
        model.addAttribute("pays", personneService.findAllPays());
        return "personne/client-saisie";
    }
    
    @PostMapping("/client/saisie")
    public String clientSaisie(
            @RequestParam(required = false) Integer idClient,
            @RequestParam String nom,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateNaissance,
            @RequestParam(required = false) Integer paysId,
            @RequestParam(required = false) String passeport,
            RedirectAttributes redirectAttributes) {
        
        try {
            Client client;
            if (idClient != null) {
                client = personneService.modifierClient(idClient, nom, email, dateNaissance, paysId, passeport);
                redirectAttributes.addFlashAttribute("success", "Client modifié avec succès");
            } else {
                client = personneService.creerClient(nom, email, dateNaissance, paysId, passeport);
                redirectAttributes.addFlashAttribute("success", "Client créé avec succès");
            }
            return "redirect:/personne/client/" + client.getIdClient();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur: " + e.getMessage());
            return "redirect:/personne/client/saisie";
        }
    }
    
    @PostMapping("/client/{id}/supprimer")
    public String clientSupprimer(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            personneService.deleteClient(id);
            redirectAttributes.addFlashAttribute("success", "Client supprimé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression: " + e.getMessage());
        }
        return "redirect:/personne/client";
    }
    
    // ==================== EMPLOYE ====================
    
    @GetMapping("/employe")
    public String employeListe(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) Integer posteId,
            @RequestParam(required = false) Integer paysId,
            Model model) {
        
        if (nom != null || posteId != null || paysId != null) {
            model.addAttribute("employes", personneService.filtrerEmployes(nom, posteId, paysId));
        } else {
            model.addAttribute("employes", personneService.findAllEmployes());
        }
        
        model.addAttribute("postes", personneService.findAllPostes());
        model.addAttribute("pays", personneService.findAllPays());
        model.addAttribute("nomFiltre", nom);
        model.addAttribute("posteIdFiltre", posteId);
        model.addAttribute("paysIdFiltre", paysId);
        
        return "personne/employe-liste";
    }
    
    @GetMapping("/employe/{id}")
    public String employeFiche(@PathVariable Integer id, Model model) {
        Employe employe = personneService.findEmployeById(id)
            .orElseThrow(() -> new RuntimeException("Employé non trouvé"));
        
        model.addAttribute("employe", employe);
        return "personne/employe-fiche";
    }
    
    @GetMapping("/employe/saisie")
    public String employeSaisieForm(Model model) {
        model.addAttribute("postes", personneService.findAllPostes());
        model.addAttribute("pays", personneService.findAllPays());
        return "personne/employe-saisie";
    }
    
    @GetMapping("/employe/saisie/{id}")
    public String employeSaisieEditForm(@PathVariable Integer id, Model model) {
        Employe employe = personneService.findEmployeById(id)
            .orElseThrow(() -> new RuntimeException("Employé non trouvé"));
        
        model.addAttribute("employe", employe);
        model.addAttribute("postes", personneService.findAllPostes());
        model.addAttribute("pays", personneService.findAllPays());
        return "personne/employe-saisie";
    }
    
    @PostMapping("/employe/saisie")
    public String employeSaisie(
            @RequestParam(required = false) Integer idEmploye,
            @RequestParam String nom,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateNaissance,
            @RequestParam(required = false) Integer paysId,
            @RequestParam Integer posteId,
            RedirectAttributes redirectAttributes) {
        
        try {
            Employe employe;
            if (idEmploye != null) {
                employe = personneService.modifierEmploye(idEmploye, nom, email, dateNaissance, paysId, posteId);
                redirectAttributes.addFlashAttribute("success", "Employé modifié avec succès");
            } else {
                employe = personneService.creerEmploye(nom, email, dateNaissance, paysId, posteId);
                redirectAttributes.addFlashAttribute("success", "Employé créé avec succès");
            }
            return "redirect:/personne/employe/" + employe.getIdEmploye();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur: " + e.getMessage());
            return "redirect:/personne/employe/saisie";
        }
    }
    
    @PostMapping("/employe/{id}/supprimer")
    public String employeSupprimer(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            personneService.deleteEmploye(id);
            redirectAttributes.addFlashAttribute("success", "Employé supprimé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression: " + e.getMessage());
        }
        return "redirect:/personne/employe";
    }
    
    // ==================== EQUIPAGE ====================
    
    @GetMapping("/equipage")
    public String equipageListe(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            Model model) {
        
        if (nom != null || dateDebut != null || dateFin != null) {
            model.addAttribute("equipages", personneService.filtrerEquipages(nom, dateDebut, dateFin));
        } else {
            model.addAttribute("equipages", personneService.findAllEquipages());
        }
        
        model.addAttribute("nomFiltre", nom);
        model.addAttribute("dateDebutFiltre", dateDebut);
        model.addAttribute("dateFinFiltre", dateFin);
        
        return "personne/equipage-liste";
    }
    
    @GetMapping("/equipage/{id}")
    public String equipageFiche(@PathVariable Integer id, Model model) {
        Equipage equipage = personneService.findEquipageById(id)
            .orElseThrow(() -> new RuntimeException("Équipage non trouvé"));
        
        model.addAttribute("equipage", equipage);
        model.addAttribute("membres", personneService.findMembresByEquipage(id));
        return "personne/equipage-fiche";
    }
    
    @GetMapping("/equipage/saisie")
    public String equipageSaisieForm(Model model) {
        model.addAttribute("equipage", new Equipage());
        return "personne/equipage-saisie";
    }
    
    @GetMapping("/equipage/saisie/{id}")
    public String equipageSaisieEditForm(@PathVariable Integer id, Model model) {
        Equipage equipage = personneService.findEquipageById(id)
            .orElseThrow(() -> new RuntimeException("Équipage non trouvé"));
        
        model.addAttribute("equipage", equipage);
        return "personne/equipage-saisie";
    }
    
    @PostMapping("/equipage/saisie")
    public String equipageSaisie(@ModelAttribute Equipage equipage, RedirectAttributes redirectAttributes) {
        try {
            Equipage saved = personneService.saveEquipage(equipage);
            redirectAttributes.addFlashAttribute("success", 
                equipage.getIdEquipage() != null ? "Équipage modifié avec succès" : "Équipage créé avec succès");
            return "redirect:/personne/equipage/" + saved.getIdEquipage();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur: " + e.getMessage());
            return "redirect:/personne/equipage/saisie";
        }
    }
    
    @PostMapping("/equipage/{id}/supprimer")
    public String equipageSupprimer(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            personneService.deleteEquipage(id);
            redirectAttributes.addFlashAttribute("success", "Équipage supprimé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression: " + e.getMessage());
        }
        return "redirect:/personne/equipage";
    }
    
    // ==================== EQUIPAGE MEMBRE ====================
    
    @GetMapping("/equipage/{id}/membre-saisie")
    public String equipageMembreSaisieForm(@PathVariable Integer id, Model model) {
        Equipage equipage = personneService.findEquipageById(id)
            .orElseThrow(() -> new RuntimeException("Équipage non trouvé"));
        
        model.addAttribute("equipage", equipage);
        model.addAttribute("membres", personneService.findMembresByEquipage(id));
        model.addAttribute("employes", personneService.findAllEmployes());
        model.addAttribute("roles", personneService.findAllRoles());
        return "personne/equipage-membre-saisie";
    }
    
    @PostMapping("/equipage/{id}/membre")
    public String equipageAjouterMembre(
            @PathVariable Integer id,
            @RequestParam Integer employeId,
            @RequestParam Integer roleId,
            @RequestParam Integer ordre,
            RedirectAttributes redirectAttributes) {
        
        try {
            personneService.ajouterMembre(id, employeId, roleId, ordre);
            redirectAttributes.addFlashAttribute("success", "Membre ajouté avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur: " + e.getMessage());
        }
        return "redirect:/personne/equipage/" + id + "/membre-saisie";
    }
    
    @PostMapping("/equipage/{equipageId}/membre/{membreId}/supprimer")
    public String equipageRetirerMembre(
            @PathVariable Integer equipageId,
            @PathVariable Integer membreId,
            RedirectAttributes redirectAttributes) {
        
        try {
            personneService.retirerMembre(membreId);
            redirectAttributes.addFlashAttribute("success", "Membre retiré avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur: " + e.getMessage());
        }
        return "redirect:/personne/equipage/" + equipageId + "/membre-saisie";
    }
}
