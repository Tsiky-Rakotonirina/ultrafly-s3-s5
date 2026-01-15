package com.itu.compagnie_aerienne.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itu.compagnie_aerienne.model.MethodePaiement;
import com.itu.compagnie_aerienne.model.Paiement;
import com.itu.compagnie_aerienne.model.PaiementMethode;
import com.itu.compagnie_aerienne.repository.PaiementMethodeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaiementMethodeService {

    private final PaiementMethodeRepository paiementMethodeRepository;

    public PaiementMethode creerPaiementMethode(Paiement paiement, MethodePaiement methodePaiement, BigDecimal montant) {
        PaiementMethode paiementMethode = new PaiementMethode();
        paiementMethode.setPaiement(paiement);
        paiementMethode.setMethodePaiement(methodePaiement);
        paiementMethode.setMontant(montant);
        return paiementMethodeRepository.save(paiementMethode);
    }

    public List<PaiementMethode> creerPaiementsMultiples(Paiement paiement, List<MethodePaiement> methodesPaiement, List<BigDecimal> montants) {
        if (methodesPaiement.size() != montants.size()) {
            throw new IllegalArgumentException("Le nombre de méthodes et de montants doit être identique");
        }

        return methodesPaiement.stream()
            .map(methode -> {
                int index = methodesPaiement.indexOf(methode);
                return creerPaiementMethode(paiement, methode, montants.get(index));
            })
            .toList();
    }
}
