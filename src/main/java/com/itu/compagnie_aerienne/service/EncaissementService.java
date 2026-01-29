package com.itu.compagnie_aerienne.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itu.compagnie_aerienne.model.Encaissement;
import com.itu.compagnie_aerienne.model.EncaissementDetail;
import com.itu.compagnie_aerienne.model.PubliciteDiffusionVol;
import com.itu.compagnie_aerienne.model.PubliciteTarrif;
import com.itu.compagnie_aerienne.model.Societe;
import com.itu.compagnie_aerienne.repository.EncaissementDetailRepository;
import com.itu.compagnie_aerienne.repository.EncaissementRepository;
import com.itu.compagnie_aerienne.repository.PubliciteDiffusionVolRepository;
import com.itu.compagnie_aerienne.repository.PubliciteTarrifRepository;
import com.itu.compagnie_aerienne.repository.SocieteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EncaissementService {
    
    private final EncaissementRepository encaissementRepository;
    private final EncaissementDetailRepository encaissementDetailRepository;
    private final PubliciteDiffusionVolRepository publiciteDiffusionVolRepository;
    private final PubliciteTarrifRepository publiciteTarrifRepository;
    private final SocieteRepository societeRepository;
    
    /**
     * Récupère toutes les sociétés
     */
    public List<Societe> getAllSocietes() {
        return societeRepository.findAll();
    }
    
    /**
     * Récupère le tarif de publicité actuel
     */
    public PubliciteTarrif getTarrifActuel() {
        List<PubliciteTarrif> tarrifs = publiciteTarrifRepository.findAll();
        // Retourne le tarif le plus récent
        return tarrifs.stream()
            .max((t1, t2) -> t1.getDateTarrif().compareTo(t2.getDateTarrif()))
            .orElse(null);
    }
    
    /**
     * Calcule le montant total à payer pour une publicité diffusion vol
     * Montant = cout_tarrif * duree * nombre
     */
    public BigDecimal calculerMontantPubliciteDiffusionVol(PubliciteDiffusionVol pdv) {
        PubliciteTarrif tarrif = getTarrifActuel();
        if (tarrif == null) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal cout = tarrif.getCout();
        BigDecimal duree = pdv.getPubliciteDiffusion().getDuree();
        Integer nombre = pdv.getNombre();
        
        if (duree == null) duree = BigDecimal.ONE;
        
        return cout.multiply(duree).multiply(new BigDecimal(nombre));
    }
    
    /**
     * Récupère le reste à payer pour une publicité diffusion vol
     */
    public BigDecimal getResteAPayer(PubliciteDiffusionVol pdv) {
        // Récupérer l'encaissement associé à cette publicité diffusion vol
        List<Encaissement> encaissements = encaissementRepository
            .findByPubliciteDiffusionVolIdPubliciteDiffusionVol(pdv.getIdPubliciteDiffusionVol());
        
        if (encaissements.isEmpty()) {
            // Pas d'encaissement, le reste à payer est le montant total
            return calculerMontantPubliciteDiffusionVol(pdv);
        }
        
        // Retourner le reste à payer du dernier encaissement
        Encaissement dernierEncaissement = encaissements.get(encaissements.size() - 1);
        return dernierEncaissement.getResteAPayer();
    }
    
    /**
     * Récupère le montant total pour une publicité diffusion vol
     * Si un encaissement existe, utilise son montant, sinon calcule avec le tarif
     */
    public BigDecimal getMontantTotal(PubliciteDiffusionVol pdv) {
        List<Encaissement> encaissements = encaissementRepository
            .findByPubliciteDiffusionVolIdPubliciteDiffusionVol(pdv.getIdPubliciteDiffusionVol());
        
        if (!encaissements.isEmpty()) {
            // Utiliser le montant du premier encaissement (montant fixé)
            return encaissements.get(0).getMontant();
        }
        
        // Sinon, calculer avec le tarif actuel
        return calculerMontantPubliciteDiffusionVol(pdv);
    }
    
    /**
     * Récupère toutes les publicités diffusion vol impayées pour une société
     */
    public List<PubliciteDiffusionVol> getPubliciteDiffusionVolImpayees(Integer societeId) {
        List<PubliciteDiffusionVol> pdvList = publiciteDiffusionVolRepository
            .findByPubliciteDiffusionSocieteIdSociete(societeId);
        
        List<PubliciteDiffusionVol> impayees = new ArrayList<>();
        for (PubliciteDiffusionVol pdv : pdvList) {
            BigDecimal resteAPayer = getResteAPayer(pdv);
            if (resteAPayer.compareTo(BigDecimal.ZERO) > 0) {
                impayees.add(pdv);
            }
        }
        return impayees;
    }
    
    /**
     * Calcule le montant total restant à payer pour une société
     */
    public BigDecimal getMontantTotalRestantSociete(Integer societeId) {
        List<PubliciteDiffusionVol> impayees = getPubliciteDiffusionVolImpayees(societeId);
        BigDecimal total = BigDecimal.ZERO;
        for (PubliciteDiffusionVol pdv : impayees) {
            total = total.add(getResteAPayer(pdv));
        }
        return total;
    }
    
    /**
     * Récupère les informations d'encaissement pour une société
     * avec les détails par publicité diffusion vol
     */
    public Map<String, Object> getEncaissementInfoSociete(Integer societeId) {
        Map<String, Object> info = new HashMap<>();
        
        Societe societe = societeRepository.findById(societeId).orElse(null);
        info.put("societe", societe);
        
        List<PubliciteDiffusionVol> impayees = getPubliciteDiffusionVolImpayees(societeId);
        info.put("publicitesImpayees", impayees);
        
        // Détails par publicité
        List<Map<String, Object>> details = new ArrayList<>();
        for (PubliciteDiffusionVol pdv : impayees) {
            Map<String, Object> detail = new HashMap<>();
            detail.put("pdv", pdv);
            detail.put("montantTotal", getMontantTotal(pdv));
            detail.put("resteAPayer", getResteAPayer(pdv));
            details.add(detail);
        }
        info.put("details", details);
        
        info.put("montantTotalRestant", getMontantTotalRestantSociete(societeId));
        
        return info;
    }
    
    /**
     * Effectue un encaissement pour une société
     * Le montant est réparti proportionnellement entre toutes les publicités diffusion vol impayées
     */
    @Transactional
    public List<EncaissementDetail> effectuerEncaissement(Integer societeId, BigDecimal montant, LocalDate dateEncaissement) {
        List<PubliciteDiffusionVol> impayees = getPubliciteDiffusionVolImpayees(societeId);
        
        if (impayees.isEmpty()) {
            throw new IllegalArgumentException("Aucune publicité impayée pour cette société");
        }
        
        // Calculer le montant total restant à payer
        BigDecimal montantTotalRestant = getMontantTotalRestantSociete(societeId);
        
        if (montant.compareTo(montantTotalRestant) > 0) {
            throw new IllegalArgumentException("Le montant dépasse le total restant à payer (" + montantTotalRestant + " Ar)");
        }
        
        // Calculer le pourcentage de paiement
        BigDecimal pourcentage = montant.divide(montantTotalRestant, 10, RoundingMode.HALF_UP);
        
        List<EncaissementDetail> encaissementDetails = new ArrayList<>();
        BigDecimal montantReparti = BigDecimal.ZERO;
        
        for (int i = 0; i < impayees.size(); i++) {
            PubliciteDiffusionVol pdv = impayees.get(i);
            BigDecimal resteAPayer = getResteAPayer(pdv);
            
            // Calculer le montant à payer pour cette publicité
            BigDecimal montantPourCettePub;
            if (i == impayees.size() - 1) {
                // Dernière publicité : on prend le reste pour éviter les erreurs d'arrondi
                montantPourCettePub = montant.subtract(montantReparti);
            } else {
                montantPourCettePub = resteAPayer.multiply(pourcentage).setScale(2, RoundingMode.HALF_UP);
            }
            montantReparti = montantReparti.add(montantPourCettePub);
            
            // Récupérer ou créer l'encaissement pour cette publicité diffusion vol
            List<Encaissement> encaissements = encaissementRepository
                .findByPubliciteDiffusionVolIdPubliciteDiffusionVol(pdv.getIdPubliciteDiffusionVol());
            
            Encaissement encaissement;
            if (encaissements.isEmpty()) {
                // Créer un nouvel encaissement
                encaissement = new Encaissement();
                encaissement.setPubliciteDiffusionVol(pdv);
                encaissement.setDateEncaissement(dateEncaissement);
                encaissement.setMontant(calculerMontantPubliciteDiffusionVol(pdv));
                encaissement.setResteAPayer(encaissement.getMontant().subtract(montantPourCettePub));
                encaissement = encaissementRepository.save(encaissement);
            } else {
                // Mettre à jour l'encaissement existant
                encaissement = encaissements.get(encaissements.size() - 1);
                BigDecimal nouveauReste = encaissement.getResteAPayer().subtract(montantPourCettePub);
                encaissement.setResteAPayer(nouveauReste);
                encaissement = encaissementRepository.save(encaissement);
            }
            
            // Créer le détail d'encaissement
            EncaissementDetail detail = new EncaissementDetail();
            detail.setEncaissement(encaissement);
            detail.setMontant(montantPourCettePub);
            detail.setDate(dateEncaissement);
            detail = encaissementDetailRepository.save(detail);
            
            encaissementDetails.add(detail);
        }
        
        return encaissementDetails;
    }
    
    /**
     * Récupère tous les encaissements
     */
    public List<Encaissement> getAllEncaissements() {
        return encaissementRepository.findAll();
    }
    
    /**
     * Récupère les détails d'un encaissement
     */
    public List<EncaissementDetail> getEncaissementDetails(Integer encaissementId) {
        return encaissementDetailRepository.findByEncaissementIdEncaissement(encaissementId);
    }
}
