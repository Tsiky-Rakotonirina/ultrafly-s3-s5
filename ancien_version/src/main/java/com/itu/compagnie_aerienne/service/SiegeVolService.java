package com.itu.compagnie_aerienne.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.itu.compagnie_aerienne.model.SiegeVol;
import com.itu.compagnie_aerienne.repository.SiegeVolRepository;

@Service
public class SiegeVolService {
    private final SiegeVolRepository siegeVolRepository;

    public SiegeVolService(SiegeVolRepository siegeVolRepository) {
        this.siegeVolRepository = siegeVolRepository;
    }

    public List<SiegeVol> getAllSiegeVols() {
        return siegeVolRepository.findAll();
    }

    public List<SiegeVol> getSiegesByVolId(Integer volId) {
        return siegeVolRepository.findAll().stream()
                .filter(siegeVol -> siegeVol.getVol().getId().equals(volId))
                .collect(Collectors.toList());
    }

    public List<SiegeVol> getSiegesByVolIdFiltered(Integer volId, String numeroSiege, Integer classeSiegeId) {
        return siegeVolRepository.findAll().stream()
                .filter(siegeVol -> siegeVol.getVol().getId().equals(volId))
                .filter(siegeVol -> numeroSiege == null || numeroSiege.isEmpty() || 
                        siegeVol.getSiege().getNumeroSiege().contains(numeroSiege))
                .filter(siegeVol -> classeSiegeId == null || 
                        siegeVol.getSiege().getClasseSiege().getId().equals(classeSiegeId))
                .collect(Collectors.toList());
    }

    public List<SiegeVol> getSiegesByVolIdWithFilters(Integer volId, Integer classeSiegeId, 
                                                       Boolean occupe, String statutPaiement) {
        return siegeVolRepository.findAll().stream()
                .filter(siegeVol -> siegeVol.getVol().getId().equals(volId))
                .filter(siegeVol -> classeSiegeId == null || 
                        siegeVol.getSiege().getClasseSiege().getId().equals(classeSiegeId))
                .filter(siegeVol -> occupe == null || siegeVol.getOccupe().equals(occupe))
                .collect(Collectors.toList());
    }
}
