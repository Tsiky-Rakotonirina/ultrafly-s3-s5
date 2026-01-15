
-- =====================================================
-- 20) INDEXES
-- =====================================================

-- Index sur les recherches fréquentes
CREATE INDEX idx_model_avion_designation ON model_avion(designation);
CREATE INDEX idx_etat_avion_libelle ON etat_avion(libelle);
CREATE INDEX idx_avion_code ON avion(code_avion);
CREATE INDEX idx_avion_model ON avion(model_avion_id);
CREATE INDEX idx_avion_etat ON avion(etat_avion_id);
CREATE INDEX idx_aeroport_code ON aeroport(code_aeroport);
CREATE INDEX idx_siege_avion ON siege(avion_id);
CREATE INDEX idx_siege_classe ON siege(classe_siege_id);
CREATE INDEX idx_vol_avion ON vol(avion_id);
CREATE INDEX idx_vol_depart ON vol(aeroport_depart_id);
CREATE INDEX idx_vol_arrivee ON vol(aeroport_arrivee_id);
CREATE INDEX idx_vol_date ON vol(date_heure_depart);
CREATE INDEX idx_prixvol_vol ON prix_vol(vol_id);
CREATE INDEX idx_prixvol_classe ON prix_vol(classe_siege_id);
CREATE INDEX idx_siegevol_vol ON siege_vol(vol_id);
CREATE INDEX idx_siegevol_siege ON siege_vol(siege_id);
CREATE INDEX idx_client_email ON client(email);
CREATE INDEX idx_client_nom_prenom ON client(nom, prenom);
CREATE INDEX idx_reservation_client ON reservation(client_id);
CREATE INDEX idx_reservation_vol ON reservation(vol_id);
CREATE INDEX idx_reservation_statut ON reservation(statut);
CREATE INDEX idx_billet_reservation ON billet(reservation_id);
CREATE INDEX idx_billet_siegevol ON billet(siege_vol_id);
CREATE INDEX idx_billet_statut ON billet(statut);
CREATE INDEX idx_paiement_reservation ON paiement(reservation_id);
CREATE INDEX idx_paiement_statut ON paiement(statut);
CREATE INDEX idx_paiement_methode_paiement ON paiement_methode(paiement_id);
