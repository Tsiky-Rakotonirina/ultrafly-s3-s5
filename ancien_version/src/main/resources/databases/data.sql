-- =====================================================
-- DONNEES DE TEST
-- =====================================================

-- Insertion des états d'avion
INSERT INTO etat_avion (libelle, description) VALUES
    ('OPERATIONNEL', 'Avion opérationnel et prêt au vol'),
    ('EN_MAINTENANCE', 'Avion en maintenance');

-- Insertion des modèles d'avion
INSERT INTO model_avion (designation, fabricant, capacite, autonomie_km, vitesse_km_h, description) VALUES
    ('Boeing 737', 'Boeing', 180, 5000, 850, 'Moyen-courrier'),
    ('Airbus A320', 'Airbus', 150, 6000, 840, 'Moyen-courrier');

-- Insertion des classes de siège
INSERT INTO classe_siege (libelle, description) VALUES 
    ('ECONOMIE', 'Classe Économique'),
    ('AFFAIRES', 'Classe Affaires');

-- Insertion des méthodes de paiement
INSERT INTO methode_paiement (libelle, description) VALUES 
    ('CARTE_BANCAIRE', 'Carte bancaire'),
    ('ESPECES', 'Espèces'),
    ('MOBILE_MONEY', 'Mobile Money');

-- Insertion des avions
INSERT INTO avion (code_avion, model_avion_id, etat_avion_id, capacite_totale) VALUES
    ('AIR-001', 1, 1, 180),
    ('AIR-002', 2, 1, 150);

-- Insertion des sièges pour AIR-001 (Boeing 737 - 180 sièges)
-- 20 sièges Affaires (1A à 5D)
INSERT INTO siege (numero_siege, classe_siege_id, avion_id) VALUES
    ('1A', 2, 1), ('1B', 2, 1), ('1C', 2, 1), ('1D', 2, 1),
    ('2A', 2, 1), ('2B', 2, 1), ('2C', 2, 1), ('2D', 2, 1),
    ('3A', 2, 1), ('3B', 2, 1), ('3C', 2, 1), ('3D', 2, 1),
    ('4A', 2, 1), ('4B', 2, 1), ('4C', 2, 1), ('4D', 2, 1),
    ('5A', 2, 1), ('5B', 2, 1), ('5C', 2, 1), ('5D', 2, 1);

-- 40 sièges Économie (10A à 19F)
INSERT INTO siege (numero_siege, classe_siege_id, avion_id) VALUES
    ('10A', 1, 1), ('10B', 1, 1), ('10C', 1, 1), ('10D', 1, 1), ('10E', 1, 1), ('10F', 1, 1),
    ('11A', 1, 1), ('11B', 1, 1), ('11C', 1, 1), ('11D', 1, 1), ('11E', 1, 1), ('11F', 1, 1),
    ('12A', 1, 1), ('12B', 1, 1), ('12C', 1, 1), ('12D', 1, 1), ('12E', 1, 1), ('12F', 1, 1),
    ('13A', 1, 1), ('13B', 1, 1), ('13C', 1, 1), ('13D', 1, 1), ('13E', 1, 1), ('13F', 1, 1),
    ('14A', 1, 1), ('14B', 1, 1), ('14C', 1, 1), ('14D', 1, 1), ('14E', 1, 1), ('14F', 1, 1),
    ('15A', 1, 1), ('15B', 1, 1), ('15C', 1, 1), ('15D', 1, 1), ('15E', 1, 1), ('15F', 1, 1),
    ('16A', 1, 1), ('16B', 1, 1), ('16C', 1, 1), ('16D', 1, 1);

-- Insertion des aéroports
INSERT INTO aeroport (code_aeroport, nom, ville, pays) VALUES
    ('TNR', 'Ivato International Airport', 'Antananarivo', 'Madagascar'),
    ('CDG', 'Charles de Gaulle', 'Paris', 'France'),
    ('JNB', 'OR Tambo International', 'Johannesburg', 'Afrique du Sud'),
    ('DXB', 'Dubai International', 'Dubai', 'Emirats Arabes Unis');

-- Insertion des vols
INSERT INTO vol (numero_vol, aeroport_depart_id, aeroport_arrivee_id, date_heure_depart, date_heure_arrivee, avion_id, status) VALUES
    ('AF001', 1, 2, '2026-01-15 08:00:00', '2026-01-15 19:00:00', 1, 'PLANIFIE'),
    ('AF002', 2, 1, '2026-01-16 20:00:00', '2026-01-17 07:00:00', 1, 'PLANIFIE'),
    ('SA100', 1, 3, '2026-01-18 10:00:00', '2026-01-18 14:00:00', 2, 'PLANIFIE');

-- Insertion des prix par vol et classe
INSERT INTO prix_vol (vol_id, classe_siege_id, prix) VALUES
    (1, 1, 450.00), -- Vol AF001 Économie
    (1, 2, 1200.00), -- Vol AF001 Affaires
    (2, 1, 480.00), -- Vol AF002 Économie
    (2, 2, 1250.00), -- Vol AF002 Affaires
    (3, 1, 350.00), -- Vol SA100 Économie
    (3, 2, 900.00); -- Vol SA100 Affaires

-- Insertion des sieges_vol pour le vol AF001
INSERT INTO siege_vol (vol_id, siege_id, occupe) 
SELECT 1, id, false FROM siege WHERE avion_id = 1;

-- Insertion des sieges_vol pour le vol AF002
INSERT INTO siege_vol (vol_id, siege_id, occupe) 
SELECT 2, id, false FROM siege WHERE avion_id = 1;

-- Insertion des sieges_vol pour le vol SA100 (avion 2)
INSERT INTO siege_vol (vol_id, siege_id, occupe) 
SELECT 3, id, false FROM siege WHERE avion_id = 2 LIMIT 60;

-- Insertion des clients
INSERT INTO client (nom, prenom, email, telephone) VALUES
    ('RAKOTO', 'Jean', 'jean.rakoto@email.mg', '+261341234567'),
    ('RABE', 'Marie', 'marie.rabe@email.mg', '+261347654321'),
    ('RANDRIA', 'Paul', 'paul.randria@email.mg', '+261349876543'),
    ('RAZAFY', 'Sophie', 'sophie.razafy@email.mg', '+261342345678'),
    ('RASOLOFO', 'Pierre', 'pierre.rasolofo@email.mg', '+261345678901');

INSERT INTO client (nom, prenom, email, telephone) VALUES
    ('RANDRIANARIVELO', 'Mahatsangy Aaron', 'noraarandrianarivelo@gmail.com', '+261385105713');

-- =====================================================
-- SCENARIO 1: Réservation payée complètement (VALIDE)
-- =====================================================
-- Client RAKOTO réserve 2 sièges en classe Affaires pour le vol AF001
INSERT INTO reservation (client_id, vol_id, date_reservation, statut) VALUES
    (1, 1, '2026-01-10 10:30:00', 'CONFIRMEE'::statut_reservation_enum);

-- Marquer les sièges comme occupés
UPDATE siege_vol SET occupe = true 
WHERE vol_id = 1 AND siege_id IN (
    SELECT id FROM siege WHERE avion_id = 1 AND numero_siege IN ('1A', '1B')
);

-- Créer les billets
INSERT INTO billet (reservation_id, siege_vol_id, prix, statut) VALUES
    (1, (SELECT id FROM siege_vol WHERE vol_id = 1 AND siege_id = (SELECT id FROM siege WHERE numero_siege = '1A' AND avion_id = 1)), 1200.00, 'EMIS'::statut_billet_enum),
    (1, (SELECT id FROM siege_vol WHERE vol_id = 1 AND siege_id = (SELECT id FROM siege WHERE numero_siege = '1B' AND avion_id = 1)), 1200.00, 'EMIS'::statut_billet_enum);

-- Créer le paiement (2400.00 total)
INSERT INTO paiement (reservation_id, montant, date_paiement, statut) VALUES
    (1, 2400.00, '2026-01-10 10:35:00', 'VALIDE'::statut_paiement_enum);

-- Enregistrer les méthodes de paiement (payé en totalité)
INSERT INTO paiement_methode (paiement_id, methode_paiement_id, montant) VALUES
    (1, 1, 2400.00); -- Payé par carte bancaire

-- =====================================================
-- SCENARIO 2: Réservation payée partiellement (EN_ATTENTE)
-- =====================================================
-- Client RABE réserve 3 sièges en classe Économie pour le vol AF001
INSERT INTO reservation (client_id, vol_id, date_reservation, statut) VALUES
    (2, 1, '2026-01-11 14:20:00', 'CONFIRMEE'::statut_reservation_enum);

-- Marquer les sièges comme occupés
UPDATE siege_vol SET occupe = true 
WHERE vol_id = 1 AND siege_id IN (
    SELECT id FROM siege WHERE avion_id = 1 AND numero_siege IN ('10A', '10B', '10C')
);

-- Créer les billets
INSERT INTO billet (reservation_id, siege_vol_id, prix, statut) VALUES
    (2, (SELECT id FROM siege_vol WHERE vol_id = 1 AND siege_id = (SELECT id FROM siege WHERE numero_siege = '10A' AND avion_id = 1)), 450.00, 'EMIS'::statut_billet_enum),
    (2, (SELECT id FROM siege_vol WHERE vol_id = 1 AND siege_id = (SELECT id FROM siege WHERE numero_siege = '10B' AND avion_id = 1)), 450.00, 'EMIS'::statut_billet_enum),
    (2, (SELECT id FROM siege_vol WHERE vol_id = 1 AND siege_id = (SELECT id FROM siege WHERE numero_siege = '10C' AND avion_id = 1)), 450.00, 'EMIS'::statut_billet_enum);

-- Créer le paiement (1350.00 total, mais seulement 500 payés)
INSERT INTO paiement (reservation_id, montant, date_paiement, statut) VALUES
    (2, 1350.00, '2026-01-11 14:25:00', 'EN_ATTENTE'::statut_paiement_enum);

-- Enregistrer paiement partiel
INSERT INTO paiement_methode (paiement_id, methode_paiement_id, montant) VALUES
    (2, 2, 500.00); -- Payé 500 en espèces (reste 850 à payer)

-- =====================================================
-- SCENARIO 3: Réservation avec paiement multiple (VALIDE)
-- =====================================================
-- Client RANDRIA réserve 1 siège Affaires + 2 Économie pour le vol AF002
INSERT INTO reservation (client_id, vol_id, date_reservation, statut) VALUES
    (3, 2, '2026-01-12 09:00:00', 'CONFIRMEE'::statut_reservation_enum);

-- Marquer les sièges comme occupés
UPDATE siege_vol SET occupe = true 
WHERE vol_id = 2 AND siege_id IN (
    SELECT id FROM siege WHERE avion_id = 1 AND numero_siege IN ('2A', '11A', '11B')
);

-- Créer les billets
INSERT INTO billet (reservation_id, siege_vol_id, prix, statut) VALUES
    (3, (SELECT id FROM siege_vol WHERE vol_id = 2 AND siege_id = (SELECT id FROM siege WHERE numero_siege = '2A' AND avion_id = 1)), 1250.00, 'EMIS'::statut_billet_enum),
    (3, (SELECT id FROM siege_vol WHERE vol_id = 2 AND siege_id = (SELECT id FROM siege WHERE numero_siege = '11A' AND avion_id = 1)), 480.00, 'EMIS'::statut_billet_enum),
    (3, (SELECT id FROM siege_vol WHERE vol_id = 2 AND siege_id = (SELECT id FROM siege WHERE numero_siege = '11B' AND avion_id = 1)), 480.00, 'EMIS'::statut_billet_enum);

-- Créer le paiement (2210.00 total)
INSERT INTO paiement (reservation_id, montant, date_paiement, statut) VALUES
    (3, 2210.00, '2026-01-12 09:10:00', 'VALIDE'::statut_paiement_enum);

-- Enregistrer paiements multiples
INSERT INTO paiement_methode (paiement_id, methode_paiement_id, montant) VALUES
    (3, 1, 1500.00), -- Carte bancaire
    (3, 3, 710.00);  -- Mobile Money

-- =====================================================
-- SCENARIO 4: Réservation sans aucun paiement (EN_ATTENTE)
-- =====================================================
-- Client RAZAFY réserve 2 sièges Économie pour le vol SA100
INSERT INTO reservation (client_id, vol_id, date_reservation, statut) VALUES
    (4, 3, '2026-01-13 16:45:00', 'CONFIRMEE'::statut_reservation_enum);

-- Marquer les sièges comme occupés
UPDATE siege_vol SET occupe = true 
WHERE vol_id = 3 AND siege_id IN (
    SELECT id FROM siege WHERE avion_id = 2 AND numero_siege IN ('10A', '10B')
);

-- Créer les billets
INSERT INTO billet (reservation_id, siege_vol_id, prix, statut) VALUES
    (4, (SELECT id FROM siege_vol WHERE vol_id = 3 AND siege_id = (SELECT id FROM siege WHERE numero_siege = '10A' AND avion_id = 2)), 350.00, 'EMIS'::statut_billet_enum),
    (4, (SELECT id FROM siege_vol WHERE vol_id = 3 AND siege_id = (SELECT id FROM siege WHERE numero_siege = '10B' AND avion_id = 2)), 350.00, 'EMIS'::statut_billet_enum);

-- Créer le paiement (700.00 total, aucun paiement effectué)
INSERT INTO paiement (reservation_id, montant, date_paiement, statut) VALUES
    (4, 700.00, '2026-01-13 16:50:00', 'EN_ATTENTE'::statut_paiement_enum);

-- Aucun paiement_methode inséré (pas encore payé)

-- =====================================================
-- FIN DU SCRIPT
-- =====================================================