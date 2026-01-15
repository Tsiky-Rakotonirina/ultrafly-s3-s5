package com.itu.compagnie_aerienne.service.avion;

import com.itu.compagnie_aerienne.model.*;
import com.itu.compagnie_aerienne.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AvionService {

    @Autowired
    private AvionRepository avionRepository;
    
    @Autowired
    private AvionSiegeRepository avionSiegeRepository;
    
    @Autowired
    private AvionHistoriqueRepository avionHistoriqueRepository;
    
    @Autowired
    private AvionCarburantRepository avionCarburantRepository;
    
    @Autowired
    private CarburantRepository carburantRepository;
    
    @Autowired
    private AvionStatutRepository avionStatutRepository;
    
    @Autowired
    private SiegeCategorieRepository siegeCategorieRepository;
    
    @Autowired
    private AeroportRepository aeroportRepository;

    // ==================== CRUD Avion ====================
    
    public List<Avion> findAll() {
        return avionRepository.findAll();
    }
    
    public Optional<Avion> findById(Integer id) {
        return avionRepository.findById(id);
    }
    
    public Optional<Avion> findByNumero(String numero) {
        return avionRepository.findByNumero(numero);
    }
    
    @Transactional
    public Avion save(Avion avion) {
        return avionRepository.save(avion);
    }
    
    @Transactional
    public void delete(Integer id) {
        avionRepository.deleteById(id);
    }

    // ==================== Filtres Avion ====================
    
    public List<Avion> filtrer(String constructeur, String modele,
                               Integer capaciteMin, Integer capaciteMax,
                               BigDecimal consommationMin, BigDecimal consommationMax,
                               LocalDate datePossessionDebut, LocalDate datePossessionFin,
                               Integer carburantId) {
        return avionRepository.filtrer(constructeur, modele,
                capaciteMin, capaciteMax, consommationMin, consommationMax,
                datePossessionDebut, datePossessionFin, carburantId);
    }

    // ==================== Gestion Sièges ====================
    
    public List<AvionSiege> getSiegesByAvion(Integer avionId) {
        return avionSiegeRepository.findByAvionIdAvion(avionId);
    }
    
    public List<AvionSiege> getSiegesByAvionAndCategorie(Integer avionId, Integer siegeCategorieId) {
        return avionSiegeRepository.findByAvionIdAvionAndSiegeCategorieIdSiegeCategorie(avionId, siegeCategorieId);
    }
    
    @Transactional
    public void genererSieges(Avion avion, int rangees, int colonnes, Integer siegeCategorieId) {
        SiegeCategorie categorie = siegeCategorieRepository.findById(siegeCategorieId)
                .orElseThrow(() -> new RuntimeException("Catégorie de siège non trouvée"));
        
        String[] lettresColonnes = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        
        for (int r = 1; r <= rangees; r++) {
            for (int c = 0; c < colonnes && c < lettresColonnes.length; c++) {
                AvionSiege siege = new AvionSiege();
                siege.setRangee(r);
                siege.setColonne(lettresColonnes[c]);
                siege.setSiegeCategorie(categorie);
                siege.setAvion(avion);
                avionSiegeRepository.save(siege);
            }
        }
    }
    
    @Transactional
    public AvionSiege saveSiege(AvionSiege siege) {
        return avionSiegeRepository.save(siege);
    }
    
    @Transactional
    public void deleteSiege(Integer id) {
        avionSiegeRepository.deleteById(id);
    }

    // ==================== Historique / Statuts ====================
    
    public List<AvionHistorique> getHistoriqueByAvion(Integer avionId) {
        return avionHistoriqueRepository.findByAvionIdAvionOrderByDateStatutDesc(avionId);
    }
    
    public Optional<AvionHistorique> getStatutActuel(Integer avionId) {
        List<AvionHistorique> historiques = avionHistoriqueRepository.findByAvionIdAvionOrderByDateStatutDesc(avionId);
        return historiques.isEmpty() ? Optional.empty() : Optional.of(historiques.get(0));
    }
    
    @Transactional
    public AvionHistorique changerStatut(Integer avionId, Integer statutId) {
        Avion avion = avionRepository.findById(avionId)
                .orElseThrow(() -> new RuntimeException("Avion non trouvé"));
        AvionStatut statut = avionStatutRepository.findById(statutId)
                .orElseThrow(() -> new RuntimeException("Statut non trouvé"));
        
        AvionHistorique historique = new AvionHistorique();
        historique.setAvion(avion);
        historique.setAvionStatut(statut);
        historique.setDateStatut(LocalDate.now());
        
        return avionHistoriqueRepository.save(historique);
    }

    // ==================== Ravitaillement Carburant ====================
    
    public List<AvionCarburant> getRavitaillementsByAvion(Integer avionId) {
        return avionCarburantRepository.findByAvionIdAvionOrderByDateCarburantDesc(avionId);
    }
    
    @Transactional
    public AvionCarburant ravitailler(Integer avionId, Integer carburantId, BigDecimal quantite) {
        Avion avion = avionRepository.findById(avionId)
                .orElseThrow(() -> new RuntimeException("Avion non trouvé"));
        Carburant carburant = carburantRepository.findById(carburantId)
                .orElseThrow(() -> new RuntimeException("Carburant non trouvé"));
        
        AvionCarburant ravitaillement = new AvionCarburant();
        ravitaillement.setAvion(avion);
        ravitaillement.setCarburant(carburant);
        ravitaillement.setQuantite(quantite);
        ravitaillement.setDateCarburant(LocalDate.now());
        
        return avionCarburantRepository.save(ravitaillement);
    }

    // ==================== Données de référence ====================
    
    public List<Carburant> getAllCarburants() {
        return carburantRepository.findAll();
    }
    
    public List<AvionStatut> getAllStatuts() {
        return avionStatutRepository.findAll();
    }
    
    public List<SiegeCategorie> getAllSiegeCategories() {
        return siegeCategorieRepository.findAll();
    }
    
    public List<Aeroport> getAllAeroports() {
        return aeroportRepository.findAll();
    }
    
    // ==================== Stats ====================
    
    public int countSiegesByAvion(Integer avionId) {
        return avionSiegeRepository.findByAvionIdAvion(avionId).size();
    }
    
    public BigDecimal getTotalCarburantByAvion(Integer avionId) {
        return avionCarburantRepository.findByAvionIdAvionOrderByDateCarburantDesc(avionId)
                .stream()
                .map(AvionCarburant::getQuantite)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
