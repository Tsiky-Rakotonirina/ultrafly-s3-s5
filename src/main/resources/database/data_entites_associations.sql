-- avion
INSERT INTO avion (id_avion, modele, constructeur, consommation, vitesse, capacite, date_possession, carburant_id) VALUES (1, 'Boeing 737-800', 'Boeing', 2500.00, 910.00, 189, '2015-06-20', 1);

-- avion_siege
INSERT INTO avion_siege (id_avion_siege, colonne, rangee, siege_categorie_id, avion_id) VALUES (1, 'A', 1, 2, 1);
INSERT INTO avion_siege (id_avion_siege, colonne, rangee, siege_categorie_id, avion_id) VALUES (2, 'B', 1, 2, 1);
INSERT INTO avion_siege (id_avion_siege, colonne, rangee, siege_categorie_id, avion_id) VALUES (3, 'C', 1, 2, 1);
INSERT INTO avion_siege (id_avion_siege, colonne, rangee, siege_categorie_id, avion_id) VALUES (4, 'D', 2, 1, 1);
INSERT INTO avion_siege (id_avion_siege, colonne, rangee, siege_categorie_id, avion_id) VALUES (5, 'E', 2, 1, 1);
INSERT INTO avion_siege (id_avion_siege, colonne, rangee, siege_categorie_id, avion_id) VALUES (6, 'F', 2, 1, 1);

-- itineraire
INSERT INTO itineraire (id_itineraire, duree, distance, aeroport_depart_id, aeroport_arrive_id) VALUES (1, '02:30:00', 450.00, 1, 3);


-- itineraire_escale
INSERT INTO itineraire_escale (id_itineraire_escale, duree, distance, aeroport_depart_id, aeroport_arrive_id, itineraire_id) VALUES (1, '00:45:00', 300.00, 1, 3, 1);


-- personne
INSERT INTO personne (id_personne, nom, email, date_naissance, date_personne, pays_id) VALUES (1, 'Jean Dupont', 'jean.dupont@email.com', '1980-05-15', '2025-01-01', 2);
INSERT INTO personne (id_personne, nom, email, date_naissance, date_personne, pays_id) VALUES (2, 'Marie Martin', 'marie.martin@email.com', '1985-08-22', '2025-01-01', 1);
INSERT INTO personne (id_personne, nom, email, date_naissance, date_personne, pays_id) VALUES (3, 'Pierre Laurent', 'pierre.laurent@email.com', '1978-12-10', '2025-01-01', 2);
INSERT INTO personne (id_personne, nom, email, date_naissance, date_personne, pays_id) VALUES (4, 'Sophie Lefevre', 'sophie.lefevre@email.com', '1990-03-28', '2025-01-01', 1);
INSERT INTO personne (id_personne, nom, email, date_naissance, date_personne, pays_id) VALUES (5, 'Claude Durand', 'claude.durand@email.com', '1982-07-05', '2025-01-01', 2);
INSERT INTO personne (id_personne, nom, email, date_naissance, date_personne, pays_id) VALUES (6, 'Francoise Moreau', 'francoise.moreau@email.com', '1988-11-14', '2025-01-01', 1);
INSERT INTO personne (id_personne, nom, email, date_naissance, date_personne, pays_id) VALUES (7, 'Andre Garcia', 'andre.garcia@email.com', '1979-06-19', '2025-01-01', 2);

-- client
INSERT INTO client (id_client, passeport, personne_id) VALUES (1, 'FA123456', 1);
INSERT INTO client (id_client, passeport, personne_id) VALUES (2, 'M987654', 2);
INSERT INTO client (id_client, passeport, personne_id) VALUES (3, 'F234567', 3);
INSERT INTO client (id_client, passeport, personne_id) VALUES (4, 'M345678', 4);

-- employe
INSERT INTO employe (id_employe, poste_id, personne_id) VALUES (1, 1, 5);
INSERT INTO employe (id_employe, poste_id, personne_id) VALUES (2, 2, 6);
INSERT INTO employe (id_employe, poste_id, personne_id) VALUES (3, 3, 7);
INSERT INTO employe (id_employe, poste_id, personne_id) VALUES (4, 4, 4);

-- equipage
INSERT INTO equipage (id_equipage, nom, date_equipage) VALUES (1, 'Equipage vol Tanananarivo-Toliara', '2025-01-10');
INSERT INTO equipage (id_equipage, nom, date_equipage) VALUES (2, 'Equipage vol Tanananarivo-Nosy Be', '2025-01-10');
INSERT INTO equipage (id_equipage, nom, date_equipage) VALUES (3, 'Equipage vol International', '2025-01-12');

-- equipage_membre
INSERT INTO equipage_membre (id_equipage_membre, ordre, equipage_id, employe_id, role_id) VALUES (1, 1, 1, 1, 1);
INSERT INTO equipage_membre (id_equipage_membre, ordre, equipage_id, employe_id, role_id) VALUES (2, 2, 1, 2, 2);
INSERT INTO equipage_membre (id_equipage_membre, ordre, equipage_id, employe_id, role_id) VALUES (3, 3, 1, 3, 3);
INSERT INTO equipage_membre (id_equipage_membre, ordre, equipage_id, employe_id, role_id) VALUES (4, 1, 2, 1, 1);
INSERT INTO equipage_membre (id_equipage_membre, ordre, equipage_id, employe_id, role_id) VALUES (5, 2, 2, 2, 2);
INSERT INTO equipage_membre (id_equipage_membre, ordre, equipage_id, employe_id, role_id) VALUES (6, 1, 3, 1, 1);

-- vol
INSERT INTO vol (id_vol, heure, vol_type_id, itineraire_id, statut_vol_id) VALUES (1, '2025-01-15 08:00:00', 1, 1, 1);


-- vol_detail
INSERT INTO vol_detail (id_vol_detail, heure, vol_id, itineraire_escale_id, avion_id, equipage_id) VALUES (1, '2025-01-15 08:00:00', 1, 1, 1, 1);


-- vol_tarrif
INSERT INTO vol_tarrif (id_vol_tarrif, prix, siege_categorie_id, vol_id) VALUES (1, 1200000.00, 3, 1);
INSERT INTO vol_tarrif (id_vol_tarrif, prix, siege_categorie_id, vol_id) VALUES (2, 700000.00, 1, 1);

-- reservation
INSERT INTO reservation (id_reservation, date_reservation, client_id, vol_id, reservation_statut_id) VALUES (1, '2025-01-08', 1, 1, 2);
INSERT INTO reservation (id_reservation, date_reservation, client_id, vol_id, reservation_statut_id) VALUES (2, '2025-01-09', 2, 1, 2);


-- reservation_billet
INSERT INTO reservation_billet (id_reservation_billet, prix, avion_siege_id, reservation_id, billet_statut_id) VALUES (1, 120000.00, 4, 1, 2);
INSERT INTO reservation_billet (id_reservation_billet, prix, avion_siege_id, reservation_id, billet_statut_id) VALUES (2, 200000.00, 1, 2, 2);

-- enregistrement


-- enregistrement_bagage

-- paiement
INSERT INTO paiement (id_paiement, montant_total, reste_payer, reservation_id, enregistrement_id) VALUES (1, 120000.00, 0.00, 1, NULL);
INSERT INTO paiement (id_paiement, montant_total, reste_payer, reservation_id, enregistrement_id) VALUES (2, 200000.00, 0.00, 2, NULL);


-- paiement_detail
INSERT INTO paiement_detail (id_paiement_detail, montant, date_paiement, paiement_mode_id, devise_id, paiement_id) VALUES (1, 120000.00, '2025-01-08', 2, 1, 1);
INSERT INTO paiement_detail (id_paiement_detail, montant, date_paiement, paiement_mode_id, devise_id, paiement_id) VALUES (2, 200000.00, '2025-01-09', 3, 1, 2);


-- avion_historique


-- avion_carburant


-- vol_historique


-- reservation_historique


-- reservation_billet_historique

