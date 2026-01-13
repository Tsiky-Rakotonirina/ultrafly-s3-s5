-- avion
INSERT INTO avion (id_avion, modele, constructeur, consommation, vitesse, capacite, date_possession, carburant_id) VALUES (1, 'Boeing 737-800', 'Boeing', 2500.00, 910.00, 189, '2015-06-20', 1);
INSERT INTO avion (id_avion, modele, constructeur, consommation, vitesse, capacite, date_possession, carburant_id) VALUES (2, 'Airbus A320', 'Airbus', 2700.00, 840.00, 180, '2018-03-15', 1);
INSERT INTO avion (id_avion, modele, constructeur, consommation, vitesse, capacite, date_possession, carburant_id) VALUES (3, 'Airbus A330', 'Airbus', 4500.00, 920.00, 440, '2016-11-10', 1);
INSERT INTO avion (id_avion, modele, constructeur, consommation, vitesse, capacite, date_possession, carburant_id) VALUES (4, 'Bombardier CRJ-900', 'Bombardier', 1800.00, 850.00, 90, '2019-05-22', 1);
INSERT INTO avion (id_avion, modele, constructeur, consommation, vitesse, capacite, date_possession, carburant_id) VALUES (5, 'Boeing 777-300ER', 'Boeing', 4000.00, 950.00, 389, '2017-09-18', 1);

-- avion_siege
INSERT INTO avion_siege (id_avion_siege, colonne, rangee, siege_categorie_id, avion_id) VALUES (1, 'A', 1, 2, 1);
INSERT INTO avion_siege (id_avion_siege, colonne, rangee, siege_categorie_id, avion_id) VALUES (2, 'B', 1, 2, 1);
INSERT INTO avion_siege (id_avion_siege, colonne, rangee, siege_categorie_id, avion_id) VALUES (3, 'C', 1, 2, 1);
INSERT INTO avion_siege (id_avion_siege, colonne, rangee, siege_categorie_id, avion_id) VALUES (4, 'D', 2, 1, 1);
INSERT INTO avion_siege (id_avion_siege, colonne, rangee, siege_categorie_id, avion_id) VALUES (5, 'E', 2, 1, 1);
INSERT INTO avion_siege (id_avion_siege, colonne, rangee, siege_categorie_id, avion_id) VALUES (6, 'F', 2, 1, 1);
INSERT INTO avion_siege (id_avion_siege, colonne, rangee, siege_categorie_id, avion_id) VALUES (7, 'A', 1, 2, 2);
INSERT INTO avion_siege (id_avion_siege, colonne, rangee, siege_categorie_id, avion_id) VALUES (8, 'B', 1, 2, 2);

-- itineraire
INSERT INTO itineraire (id_itineraire, duree, distance, aeroport_depart_id, aeroport_arrive_id) VALUES (1, '02:30:00', 450.00, 1, 2);
INSERT INTO itineraire (id_itineraire, duree, distance, aeroport_depart_id, aeroport_arrive_id) VALUES (2, '01:00:00', 120.00, 1, 3);
INSERT INTO itineraire (id_itineraire, duree, distance, aeroport_depart_id, aeroport_arrive_id) VALUES (3, '02:00:00', 350.00, 1, 4);
INSERT INTO itineraire (id_itineraire, duree, distance, aeroport_depart_id, aeroport_arrive_id) VALUES (4, '05:30:00', 3800.00, 1, 6);
INSERT INTO itineraire (id_itineraire, duree, distance, aeroport_depart_id, aeroport_arrive_id) VALUES (5, '04:00:00', 2900.00, 1, 7);

-- itineraire_escale
INSERT INTO itineraire_escale (id_itineraire_escale, duree, distance, aeroport_depart_id, aeroport_arrive_id, itineraire_id) VALUES (1, '00:45:00', 300.00, 1, 2, 4);
INSERT INTO itineraire_escale (id_itineraire_escale, duree, distance, aeroport_depart_id, aeroport_arrive_id, itineraire_id) VALUES (2, '01:30:00', 550.00, 2, 6, 4);
INSERT INTO itineraire_escale (id_itineraire_escale, duree, distance, aeroport_depart_id, aeroport_arrive_id, itineraire_id) VALUES (3, '01:00:00', 450.00, 1, 7, 5);

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
INSERT INTO vol (id_vol, heure, vol_type_id, itineraire_id, statut_vol_id) VALUES (2, '2025-01-15 10:30:00', 1, 2, 1);
INSERT INTO vol (id_vol, heure, vol_type_id, itineraire_id, statut_vol_id) VALUES (3, '2025-01-16 14:00:00', 1, 3, 1);
INSERT INTO vol (id_vol, heure, vol_type_id, itineraire_id, statut_vol_id) VALUES (4, '2025-01-20 22:00:00', 2, 4, 1);
INSERT INTO vol (id_vol, heure, vol_type_id, itineraire_id, statut_vol_id) VALUES (5, '2025-01-22 18:30:00', 2, 5, 1);

-- vol_detail
INSERT INTO vol_detail (id_vol_detail, heure, vol_id, itineraire_escale_id, avion_id, equipage_id) VALUES (1, '2025-01-15 08:00:00', 1, 1, 1, 1);
INSERT INTO vol_detail (id_vol_detail, heure, vol_id, itineraire_escale_id, avion_id, equipage_id) VALUES (2, '2025-01-15 10:30:00', 2, 2, 2, 2);
INSERT INTO vol_detail (id_vol_detail, heure, vol_id, itineraire_escale_id, avion_id, equipage_id) VALUES (3, '2025-01-16 14:00:00', 3, 3, 3, 1);

-- vol_tarrif
INSERT INTO vol_tarrif (id_vol_tarrif, prix, siege_categorie_id, vol_id) VALUES (1, 120000.00, 1, 1);
INSERT INTO vol_tarrif (id_vol_tarrif, prix, siege_categorie_id, vol_id) VALUES (2, 200000.00, 2, 1);
INSERT INTO vol_tarrif (id_vol_tarrif, prix, siege_categorie_id, vol_id) VALUES (3, 100000.00, 1, 2);
INSERT INTO vol_tarrif (id_vol_tarrif, prix, siege_categorie_id, vol_id) VALUES (4, 180000.00, 2, 2);
INSERT INTO vol_tarrif (id_vol_tarrif, prix, siege_categorie_id, vol_id) VALUES (5, 850000.00, 1, 4);
INSERT INTO vol_tarrif (id_vol_tarrif, prix, siege_categorie_id, vol_id) VALUES (6, 1500000.00, 3, 4);

-- reservation
INSERT INTO reservation (id_reservation, date_reservation, client_id, vol_id, reservation_statut_id) VALUES (1, '2025-01-08', 1, 1, 2);
INSERT INTO reservation (id_reservation, date_reservation, client_id, vol_id, reservation_statut_id) VALUES (2, '2025-01-09', 2, 1, 2);
INSERT INTO reservation (id_reservation, date_reservation, client_id, vol_id, reservation_statut_id) VALUES (3, '2025-01-10', 3, 2, 2);
INSERT INTO reservation (id_reservation, date_reservation, client_id, vol_id, reservation_statut_id) VALUES (4, '2025-01-12', 4, 4, 1);
INSERT INTO reservation (id_reservation, date_reservation, client_id, vol_id, reservation_statut_id) VALUES (5, '2025-01-14', 1, 5, 2);

-- reservation_billet
INSERT INTO reservation_billet (id_reservation_billet, prix, avion_siege_id, reservation_id, billet_statut_id) VALUES (1, 120000.00, 4, 1, 2);
INSERT INTO reservation_billet (id_reservation_billet, prix, avion_siege_id, reservation_id, billet_statut_id) VALUES (2, 200000.00, 1, 2, 2);
INSERT INTO reservation_billet (id_reservation_billet, prix, avion_siege_id, reservation_id, billet_statut_id) VALUES (3, 100000.00, 7, 3, 2);
INSERT INTO reservation_billet (id_reservation_billet, prix, avion_siege_id, reservation_id, billet_statut_id) VALUES (4, 850000.00, 5, 4, 1);
INSERT INTO reservation_billet (id_reservation_billet, prix, avion_siege_id, reservation_id, billet_statut_id) VALUES (5, 1500000.00, 2, 5, 2);

-- enregistrement
INSERT INTO enregistrement (id_enregistrement, heure, client_type_id, reservation_billet_id) VALUES (1, '2025-01-15 07:00:00', 4, 1);
INSERT INTO enregistrement (id_enregistrement, heure, client_type_id, reservation_billet_id) VALUES (2, '2025-01-15 07:15:00', 4, 2);
INSERT INTO enregistrement (id_enregistrement, heure, client_type_id, reservation_billet_id) VALUES (3, '2025-01-15 10:00:00', 4, 3);

-- enregistrement_bagage
INSERT INTO enregistrement_bagage (id_enregistrement_bagage, poids, bagage_type_id, enregistrement_id) VALUES (1, 23.50, 2, 1);
INSERT INTO enregistrement_bagage (id_enregistrement_bagage, poids, bagage_type_id, enregistrement_id) VALUES (2, 20.00, 2, 2);
INSERT INTO enregistrement_bagage (id_enregistrement_bagage, poids, bagage_type_id, enregistrement_id) VALUES (3, 15.00, 2, 3);
INSERT INTO enregistrement_bagage (id_enregistrement_bagage, poids, bagage_type_id, enregistrement_id) VALUES (4, 5.50, 1, 1);

-- paiement
INSERT INTO paiement (id_paiement, montant_total, reste_payer, reservation_id, enregistrement_id) VALUES (1, 120000.00, 0.00, 1, NULL);
INSERT INTO paiement (id_paiement, montant_total, reste_payer, reservation_id, enregistrement_id) VALUES (2, 200000.00, 0.00, 2, NULL);
INSERT INTO paiement (id_paiement, montant_total, reste_payer, reservation_id, enregistrement_id) VALUES (3, 100000.00, 0.00, 3, NULL);
INSERT INTO paiement (id_paiement, montant_total, reste_payer, reservation_id, enregistrement_id) VALUES (4, 850000.00, 425000.00, 4, NULL);

-- paiement_detail
INSERT INTO paiement_detail (id_paiement_detail, montant, date_paiement, paiement_mode_id, devise_id, paiement_id) VALUES (1, 120000.00, '2025-01-08', 2, 1, 1);
INSERT INTO paiement_detail (id_paiement_detail, montant, date_paiement, paiement_mode_id, devise_id, paiement_id) VALUES (2, 200000.00, '2025-01-09', 3, 1, 2);
INSERT INTO paiement_detail (id_paiement_detail, montant, date_paiement, paiement_mode_id, devise_id, paiement_id) VALUES (3, 100000.00, '2025-01-10', 4, 1, 3);
INSERT INTO paiement_detail (id_paiement_detail, montant, date_paiement, paiement_mode_id, devise_id, paiement_id) VALUES (4, 425000.00, '2025-01-12', 2, 1, 4);

-- avion_historique
INSERT INTO avion_historique (id_avion_historique, date_statut, avion_id, avion_statut_id) VALUES (1, '2025-01-01', 1, 1);
INSERT INTO avion_historique (id_avion_historique, date_statut, avion_id, avion_statut_id) VALUES (2, '2025-01-01', 2, 1);
INSERT INTO avion_historique (id_avion_historique, date_statut, avion_id, avion_statut_id) VALUES (3, '2025-01-01', 3, 1);
INSERT INTO avion_historique (id_avion_historique, date_statut, avion_id, avion_statut_id) VALUES (4, '2025-01-01', 4, 1);
INSERT INTO avion_historique (id_avion_historique, date_statut, avion_id, avion_statut_id) VALUES (5, '2025-01-01', 5, 1);

-- avion_carburant
INSERT INTO avion_carburant (id_avion_carburant, quantite, date_carburant, avion_id, carburant_id) VALUES (1, 2500.00, '2025-01-15', 1, 1);
INSERT INTO avion_carburant (id_avion_carburant, quantite, date_carburant, avion_id, carburant_id) VALUES (2, 2300.00, '2025-01-15', 2, 1);
INSERT INTO avion_carburant (id_avion_carburant, quantite, date_carburant, avion_id, carburant_id) VALUES (3, 4000.00, '2025-01-16', 3, 1);
INSERT INTO avion_carburant (id_avion_carburant, quantite, date_carburant, avion_id, carburant_id) VALUES (4, 1600.00, '2025-01-15', 4, 1);
INSERT INTO avion_carburant (id_avion_carburant, quantite, date_carburant, avion_id, carburant_id) VALUES (5, 3800.00, '2025-01-16', 5, 1);

-- vol_historique
INSERT INTO vol_historique (id_vol_historique, date_statut, vol_id, statut_vol_id) VALUES (1, '2025-01-15', 1, 1);
INSERT INTO vol_historique (id_vol_historique, date_statut, vol_id, statut_vol_id) VALUES (2, '2025-01-15', 1, 2);
INSERT INTO vol_historique (id_vol_historique, date_statut, vol_id, statut_vol_id) VALUES (3, '2025-01-15', 1, 3);
INSERT INTO vol_historique (id_vol_historique, date_statut, vol_id, statut_vol_id) VALUES (4, '2025-01-15', 1, 4);

-- reservation_historique
INSERT INTO reservation_historique (id_reservation_historique, date_statut, reservation_id, reservation_statut_id) VALUES (1, '2025-01-08', 1, 1);
INSERT INTO reservation_historique (id_reservation_historique, date_statut, reservation_id, reservation_statut_id) VALUES (2, '2025-01-08', 1, 2);
INSERT INTO reservation_historique (id_reservation_historique, date_statut, reservation_id, reservation_statut_id) VALUES (3, '2025-01-09', 2, 1);
INSERT INTO reservation_historique (id_reservation_historique, date_statut, reservation_id, reservation_statut_id) VALUES (4, '2025-01-09', 2, 2);

-- reservation_billet_historique
INSERT INTO reservation_billet_historique (id_reservation_billet_historique, date_statut, reservation_billet_id, billet_statut_id) VALUES (1, '2025-01-08', 1, 1);
INSERT INTO reservation_billet_historique (id_reservation_billet_historique, date_statut, reservation_billet_id, billet_statut_id) VALUES (2, '2025-01-08', 1, 2);
INSERT INTO reservation_billet_historique (id_reservation_billet_historique, date_statut, reservation_billet_id, billet_statut_id) VALUES (3, '2025-01-09', 2, 1);
INSERT INTO reservation_billet_historique (id_reservation_billet_historique, date_statut, reservation_billet_id, billet_statut_id) VALUES (4, '2025-01-09', 2, 2);
