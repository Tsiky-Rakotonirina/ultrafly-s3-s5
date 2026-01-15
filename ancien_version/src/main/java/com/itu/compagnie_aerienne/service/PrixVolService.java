package com.itu.compagnie_aerienne.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.itu.compagnie_aerienne.model.PrixVol;
import com.itu.compagnie_aerienne.model.SiegeVol;
import com.itu.compagnie_aerienne.repository.PrixVolRepository;

@Service
public class PrixVolService {

    public final PrixVolRepository prixVolRepository;

    public PrixVolService(PrixVolRepository prixVolRepository) {
        this.prixVolRepository = prixVolRepository;
    }

    public List<PrixVol> getAllPrixVolByVolId(Integer volId) {
        return prixVolRepository.findByVolId(volId);
    }

    /**
     * Obtenir le prix d'un vol pour une classe de siège spécifique
     * @param volId l'identifiant du vol
     * @param classeSiegeId l'identifiant de la classe de siège
     * @return le PrixVol correspondant ou Optional.empty() si non trouvé
     */
    public Optional<PrixVol> getPrixVolByVolIdAndClasseSiegeId(Integer volId, Integer classeSiegeId) {
        return prixVolRepository.findByVolIdAndClasseSiegeId(volId, classeSiegeId);
    }

    /**
     * Obtenir le prix d'un siège pour un vol donné
     * @param siegeVol le siège du vol
     * @return le prix du siège ou BigDecimal.ZERO si non trouvé
     */
    public BigDecimal getPrixForSiegeVol(SiegeVol siegeVol) {
        Integer volId = siegeVol.getVol().getId();
        Integer classeSiegeId = siegeVol.getSiege().getClasseSiege().getId();
        
        return getPrixVolByVolIdAndClasseSiegeId(volId, classeSiegeId)
                .map(PrixVol::getPrix)
                .orElse(BigDecimal.ZERO);
    }

    /**
     * Créer une map des prix pour tous les sièges d'un vol
     * @param siegesVol liste des sièges du vol
     * @return Map avec siegeVolId comme clé et prix comme valeur
     */
    public Map<Integer, BigDecimal> getPrixMapForSiegesVol(List<SiegeVol> siegesVol) {
        Map<Integer, BigDecimal> prixMap = new HashMap<>();
        
        for (SiegeVol siegeVol : siegesVol) {
            BigDecimal prix = getPrixForSiegeVol(siegeVol);
            prixMap.put(siegeVol.getId(), prix);
        }
        
        return prixMap;
    }

    /**
     * Calculer le prix total pour une liste de sièges sélectionnés
     * @param siegesVol liste des sièges sélectionnés
     * @return le prix total
     */
    public BigDecimal calculerPrixTotal(List<SiegeVol> siegesVol) {
        return siegesVol.stream()
                .map(this::getPrixForSiegeVol)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
