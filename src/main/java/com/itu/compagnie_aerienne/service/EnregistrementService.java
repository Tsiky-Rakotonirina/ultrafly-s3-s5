package com.itu.compagnie_aerienne.service;

import com.itu.compagnie_aerienne.model.*;
import com.itu.compagnie_aerienne.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EnregistrementService {
    
    @Autowired
    private EnregistrementRepository enregistrementRepository;
    
    @Autowired
    private EnregistrementBagageRepository enregistrementBagageRepository;
    
    @Autowired
    private ReservationBilletRepository reservationBilletRepository;
    
    @Autowired
    private ClientTypeRepository clientTypeRepository;
    
    @Autowired
    private BagageTypeRepository bagageTypeRepository;
    
    @Autowired
    private PaiementRepository paiementRepository;
    
    @Autowired
    private PaiementDetailRepository paiementDetailRepository;
    
    @Autowired
    private PaiementModeRepository paiementModeRepository;
    
    @Autowired
    private DeviseRepository deviseRepository;
    
    @Autowired
    private ChangeRepository changeRepository;
    
    // ==================== ENREGISTREMENT ====================
    
    public List<Enregistrement> findAll() {
        return enregistrementRepository.findAll();
    }
    
    public Optional<Enregistrement> findById(Integer id) {
        return enregistrementRepository.findById(id);
    }
    
    public List<Enregistrement> findByVol(Integer volId) {
        return enregistrementRepository.findByVolIdVolOrderByIdEnregistrementAsc(volId);
    }
    
    public List<Enregistrement> findByReservation(Integer reservationId) {
        return enregistrementRepository.findByReservationBilletReservationIdReservationOrderByIdEnregistrementAsc(reservationId);
    }
    
    public List<Enregistrement> filtrer(String numero, Integer volId, Integer reservationId, 
                                        LocalDateTime heureDebut, LocalDateTime heureFin,
                                        String status) {
        return enregistrementRepository.filtrer(numero, volId, reservationId, heureDebut, heureFin, status);
    }
    
    public Enregistrement creerEnregistrement(Integer reservationBilletId, Integer clientTypeId) {
        ReservationBillet billet = reservationBilletRepository.findById(reservationBilletId)
            .orElseThrow(() -> new RuntimeException("Billet non trouvé"));
        
        ClientType clientType = clientTypeRepository.findById(clientTypeId)
            .orElseThrow(() -> new RuntimeException("Type de client non trouvé"));
        
        Enregistrement enregistrement = new Enregistrement();
        enregistrement.setReservationBillet(billet);
        enregistrement.setClientType(clientType);
        enregistrement.setHeureEnregistrement(LocalDateTime.now());
        enregistrement.setStatutEnregistrement("Ouvert");
        
        return enregistrementRepository.save(enregistrement);
    }
    
    public void clorerEnregistrement(Integer enregistrementId) {
        Enregistrement enregistrement = enregistrementRepository.findById(enregistrementId)
            .orElseThrow(() -> new RuntimeException("Enregistrement non trouvé"));
        
        enregistrement.setStatutEnregistrement("Clos");
        enregistrementRepository.save(enregistrement);
    }
    
    // ==================== BAGAGES ====================
    
    public List<EnregistrementBagage> findBagagesByEnregistrement(Integer enregistrementId) {
        return enregistrementBagageRepository.findByEnregistrementIdEnregistrementOrderByIdEnregistrementBagageAsc(enregistrementId);
    }
    
    public BigDecimal getTotalPoidsProforma(Integer enregistrementId) {
        List<EnregistrementBagage> bagages = findBagagesByEnregistrement(enregistrementId);
        return bagages.stream()
            .map(EnregistrementBagage::getPoids)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public BigDecimal getPoidsMaxAutorise(Integer enregistrementId) {
        Enregistrement enregistrement = enregistrementRepository.findById(enregistrementId)
            .orElseThrow(() -> new RuntimeException("Enregistrement non trouvé"));
        
        ReservationBillet billet = enregistrement.getReservationBillet();
        SiegeCategorie categorie = billet.getAvionSiege().getSiegeCategorie();
        
        // Poids par défaut selon catégorie de siège
        if ("Premiere Classe".equals(categorie.getLibelle())) {
            return BigDecimal.valueOf(40); // kg
        } else if ("Business".equals(categorie.getLibelle())) {
            return BigDecimal.valueOf(30);
        } else {
            return BigDecimal.valueOf(20); // Économique
        }
    }
    
    public boolean hasExcesSurpoids(Integer enregistrementId) {
        BigDecimal totalPoids = getTotalPoidsProforma(enregistrementId);
        BigDecimal poidsMax = getPoidsMaxAutorise(enregistrementId);
        
        return totalPoids.compareTo(poidsMax) > 0;
    }
    
    public BigDecimal getExcesSurpoids(Integer enregistrementId) {
        BigDecimal totalPoids = getTotalPoidsProforma(enregistrementId);
        BigDecimal poidsMax = getPoidsMaxAutorise(enregistrementId);
        
        BigDecimal exces = totalPoids.subtract(poidsMax);
        return exces.compareTo(BigDecimal.ZERO) > 0 ? exces : BigDecimal.ZERO;
    }
    
    public EnregistrementBagage ajouterBagage(Integer enregistrementId, Integer bagageTypeId, BigDecimal poids) {
        Enregistrement enregistrement = enregistrementRepository.findById(enregistrementId)
            .orElseThrow(() -> new RuntimeException("Enregistrement non trouvé"));
        
        BagageType bagageType = bagageTypeRepository.findById(bagageTypeId)
            .orElseThrow(() -> new RuntimeException("Type de bagage non trouvé"));
        
        EnregistrementBagage bagage = new EnregistrementBagage();
        bagage.setEnregistrement(enregistrement);
        bagage.setBagageType(bagageType);
        bagage.setPoids(poids);
        
        return enregistrementBagageRepository.save(bagage);
    }
    
    public void supprimerBagage(Integer bagageId) {
        enregistrementBagageRepository.deleteById(bagageId);
    }
    
    // ==================== TARIFS EXCÉDENTS ====================
    
    public BigDecimal calculerFraisSurpoids(Integer enregistrementId) {
        BigDecimal excesSurpoids = getExcesSurpoids(enregistrementId);
        
        if (excesSurpoids.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        
        // Tarif par kg excédentaire : 5000 MGA/kg
        return excesSurpoids.multiply(BigDecimal.valueOf(5000));
    }
    
    public void appliqueFraisSurpoids(Integer enregistrementId, Integer paiementModeId, Integer deviseId) {
        BigDecimal frais = calculerFraisSurpoids(enregistrementId);
        
        if (frais.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }
        
        Enregistrement enregistrement = enregistrementRepository.findById(enregistrementId)
            .orElseThrow(() -> new RuntimeException("Enregistrement non trouvé"));
        
        // Créer un paiement pour les frais
        Paiement paiement = new Paiement();
        paiement.setEnregistrement(enregistrement);
        paiement.setMontantTotal(frais);
        paiement.setRestePayer(frais);
        
        paiement = paiementRepository.save(paiement);
        
        // Enregistrer le détail du paiement
        PaiementDetail detail = new PaiementDetail();
        detail.setPaiement(paiement);
        detail.setMontant(frais);
        detail.setDevise(deviseRepository.findById(deviseId).orElse(null));
        detail.setPaiementMode(paiementModeRepository.findById(paiementModeId).orElse(null));
        detail.setDatePaiement(LocalDate.now());
        
        paiementDetailRepository.save(detail);
        
        // Marquer comme payé
        paiement.setRestePayer(BigDecimal.ZERO);
        paiementRepository.save(paiement);
    }
    
    // ==================== STATUT BILLET ====================
    
    public void marquerBilletEmbarque(Integer enregistrementId) {
        Enregistrement enregistrement = enregistrementRepository.findById(enregistrementId)
            .orElseThrow(() -> new RuntimeException("Enregistrement non trouvé"));
        
        ReservationBillet billet = enregistrement.getReservationBillet();
        billet.getBilletStatut().setLibelle("Embarque");
        reservationBilletRepository.save(billet);
    }
    
    // ==================== REFERENCES ====================
    
    public List<BagageType> findAllBagageTypes() {
        return bagageTypeRepository.findAll();
    }
    
    public List<ClientType> findAllClientTypes() {
        return clientTypeRepository.findAll();
    }
    
    public List<Devise> findAllDevises() {
        return deviseRepository.findAll();
    }
    
    public List<PaiementMode> findAllPaiementModes() {
        return paiementModeRepository.findAll();
    }
}
