-- Table avion_statut
INSERT INTO avion_statut (libelle, description) VALUES ('En service', 'Avion operationnel et disponible');
INSERT INTO avion_statut (libelle, description) VALUES ('En maintenance', 'Avion en revision technique');
INSERT INTO avion_statut (libelle, description) VALUES ('Hors service', 'Avion immobilise');
INSERT INTO avion_statut (libelle, description) VALUES ('En attente', 'Avion en attente de validation');

-- Table siege_categorie
INSERT INTO siege_categorie (libelle, description) VALUES ('economique', 'Classe economique standard');
INSERT INTO siege_categorie (libelle, description) VALUES ('Affaires', 'Classe affaires avec plus de confort');
INSERT INTO siege_categorie (libelle, description) VALUES ('Première', 'Classe première avec services premium');

-- Table carburant
INSERT INTO carburant (libelle, description) VALUES ('Jet A-1', 'Kerosène aviation standard');
INSERT INTO carburant (libelle, description) VALUES ('Jet A', 'Kerosène aviation alternatif');
INSERT INTO carburant (libelle, description) VALUES ('AvGas', 'Essence aviation pour petits appareils');

-- Table vol_type
INSERT INTO vol_type (libelle, description) VALUES ('Interieur', 'Vol national à Madagascar');
INSERT INTO vol_type (libelle, description) VALUES ('International', 'Vol vers l''etranger');

-- Table vol_statut
INSERT INTO vol_statut (libelle, description) VALUES ('Programme', 'Vol planifie');
INSERT INTO vol_statut (libelle, description) VALUES ('Embarquement', 'Embarquement en cours');
INSERT INTO vol_statut (libelle, description) VALUES ('Decolle', 'Vol en cours');
INSERT INTO vol_statut (libelle, description) VALUES ('Atterri', 'Vol termine');
INSERT INTO vol_statut (libelle, description) VALUES ('Annule', 'Vol annule');
INSERT INTO vol_statut (libelle, description) VALUES ('Retarde', 'Vol retarde');
INSERT INTO vol_statut (libelle, description) VALUES ('Reporte', 'Vol reporte');

-- Table vol_report_type
INSERT INTO vol_report_type (libelle, description) VALUES ('Meteo', 'Reporte pour conditions meteorologiques');
INSERT INTO vol_report_type (libelle, description) VALUES ('Technique', 'Reporte pour problème technique');
INSERT INTO vol_report_type (libelle, description) VALUES ('Operationnel', 'Reporte pour raison operationnelle');

-- Table poste (équipage à bord uniquement)
INSERT INTO poste (libelle, description) VALUES ('Pilote', 'Pilote de ligne');
INSERT INTO poste (libelle, description) VALUES ('Copilote', 'Second pilote');
INSERT INTO poste (libelle, description) VALUES ('Hôtesse', 'Personnel de cabine féminin');
INSERT INTO poste (libelle, description) VALUES ('Steward', 'Personnel de cabine masculin');
INSERT INTO poste (libelle, description) VALUES ('Mécanicien navigant', 'Ingénieur de vol à bord');
INSERT INTO poste (libelle, description) VALUES ('Infirmier de bord', 'Personnel médical à bord');
INSERT INTO poste (libelle, description) VALUES ('Agent de sûreté aérien', 'Sécurité à bord du vol');
INSERT INTO poste (libelle, description) VALUES ('Purser', 'Responsable de cabine principal');
INSERT INTO poste (libelle, description) VALUES ('Commissaire de bord', 'Responsable service passagers');

-- Table role (rôles à bord uniquement)
INSERT INTO role (libelle, description) VALUES ('Commandant de bord', 'Responsable du vol et de l''appareil');
INSERT INTO role (libelle, description) VALUES ('Officier pilote de ligne', 'Copilote qualifié');
INSERT INTO role (libelle, description) VALUES ('Chef de cabine', 'Responsable de l''équipage cabine');
INSERT INTO role (libelle, description) VALUES ('Agent de bord', 'Service aux passagers');
INSERT INTO role (libelle, description) VALUES ('Responsable zone cabine', 'Supervision d''une section de la cabine');
INSERT INTO role (libelle, description) VALUES ('Ingénieur de vol', 'Gestion des systèmes techniques en vol');
INSERT INTO role (libelle, description) VALUES ('Responsable médical de bord', 'Gestion des urgences médicales');
INSERT INTO role (libelle, description) VALUES ('Agent de sécurité de bord', 'Protection et sûreté du vol');
INSERT INTO role (libelle, description) VALUES ('Instructeur en vol', 'Formation pratique à bord');
INSERT INTO role (libelle, description) VALUES ('Pilote de relève', 'Pilote supplémentaire pour vols longs');

-- Table client_type
INSERT INTO client_type (libelle, description) VALUES ('Bebe', 'Passager de 0 à 1 an');
INSERT INTO client_type (libelle, description) VALUES ('Enfant', 'Passager de 2 à 11 ans');
INSERT INTO client_type (libelle, description) VALUES ('Adolescent', 'Passager de 12 à 17 ans');
INSERT INTO client_type (libelle, description) VALUES ('Adulte', 'Passager de 18 à 64 ans');
INSERT INTO client_type (libelle, description) VALUES ('Personne âgee', 'Passager de 65 ans et plus');
INSERT INTO client_type (libelle, description) VALUES ('Personne handicapee', 'Passager à mobilite reduite');
INSERT INTO client_type (libelle, description) VALUES ('Personne malade', 'Passager necessitant assistance medicale');

-- Table reservation_statut
INSERT INTO reservation_statut (libelle, description) VALUES ('En attente', 'Reservation en attente de paiement');
INSERT INTO reservation_statut (libelle, description) VALUES ('Confirmee', 'Reservation validee');
INSERT INTO reservation_statut (libelle, description) VALUES ('Annulee', 'Reservation annulee');
INSERT INTO reservation_statut (libelle, description) VALUES ('Expiree', 'Reservation expiree');

-- Table billet_statut
INSERT INTO billet_statut (libelle, description) VALUES ('Emis', 'Billet en attente de paiement');
INSERT INTO billet_statut (libelle, description) VALUES ('Paye', 'Billet valide pour le vol');
INSERT INTO billet_statut (libelle, description) VALUES ('Annule', 'Billet annule');
INSERT INTO billet_statut (libelle, description) VALUES ('Expire', 'Billet expire');

-- Table bagage_type
INSERT INTO bagage_type (libelle, description) VALUES ('Cabine', 'Bagage à main');
INSERT INTO bagage_type (libelle, description) VALUES ('Soute', 'Bagage enregistre');
INSERT INTO bagage_type (libelle, description) VALUES ('Special', 'Bagage hors format');

-- Table devise
INSERT INTO devise (libelle, description) VALUES ('Ariary', 'Monnaie malgache (MGA)');
INSERT INTO devise (libelle, description) VALUES ('Euro', 'Monnaie europeenne (EUR)');
INSERT INTO devise (libelle, description) VALUES ('Dollar', 'Monnaie americaine (USD)');

-- Table paiement_mode
INSERT INTO paiement_mode (libelle, description) VALUES ('Espèces', 'Paiement en liquide');
INSERT INTO paiement_mode (libelle, description) VALUES ('Carte bancaire', 'Paiement par carte');
INSERT INTO paiement_mode (libelle, description) VALUES ('Virement', 'Paiement par virement bancaire');
INSERT INTO paiement_mode (libelle, description) VALUES ('Mobile money', 'Paiement mobile');

-- Table pays
INSERT INTO pays (nom, nationalite) VALUES ('Madagascar', 'Malgache');
INSERT INTO pays (nom, nationalite) VALUES ('France', 'Française');
INSERT INTO pays (nom, nationalite) VALUES ('Maurice', 'Mauricienne');
INSERT INTO pays (nom, nationalite) VALUES ('Reunion', 'Française');
INSERT INTO pays (nom, nationalite) VALUES ('Kenya', 'Kenyane');

-- Table ville (Madagascar)
INSERT INTO ville (nom, pays_id) VALUES ('Antananarivo', 1);
INSERT INTO ville (nom, pays_id) VALUES ('Toamasina', 1);
INSERT INTO ville (nom, pays_id) VALUES ('Nosy Be', 1);
INSERT INTO ville (nom, pays_id) VALUES ('Mahajanga', 1);
INSERT INTO ville (nom, pays_id) VALUES ('Antsiranana', 1);
-- Table ville (International)
INSERT INTO ville (nom, pays_id) VALUES ('Paris', 2);
INSERT INTO ville (nom, pays_id) VALUES ('Port-Louis', 3);
INSERT INTO ville (nom, pays_id) VALUES ('Saint-Denis', 4);
INSERT INTO ville (nom, pays_id) VALUES ('Nairobi', 5);

-- Table aeroport (Madagascar)
INSERT INTO aeroport (nom, code_iata, ville_id) VALUES ('Ivato International', 'TNR', 1);
INSERT INTO aeroport (nom, code_iata, ville_id) VALUES ('Toamasina Airport', 'TMM', 2);
INSERT INTO aeroport (nom, code_iata, ville_id) VALUES ('Fascene Airport', 'NOS', 3);
INSERT INTO aeroport (nom, code_iata, ville_id) VALUES ('Amborovy Airport', 'MJN', 4);
INSERT INTO aeroport (nom, code_iata, ville_id) VALUES ('Arrachart Airport', 'DIE', 5);
-- Table aeroport (International)
INSERT INTO aeroport (nom, code_iata, ville_id) VALUES ('Charles de Gaulle', 'CDG', 6);
INSERT INTO aeroport (nom, code_iata, ville_id) VALUES ('SSR International', 'MRU', 7);
INSERT INTO aeroport (nom, code_iata, ville_id) VALUES ('Roland Garros', 'RUN', 8);
INSERT INTO aeroport (nom, code_iata, ville_id) VALUES ('Jomo Kenyatta', 'NBO', 9);

-- Table carburant_tarif
INSERT INTO carburant_tarif (carburant_id, prix, date_tarif) VALUES (1, 4500.00, '2025-01-01');
INSERT INTO carburant_tarif (carburant_id, prix, date_tarif) VALUES (1, 4550.00, '2025-02-01');
INSERT INTO carburant_tarif (carburant_id, prix, date_tarif) VALUES (2, 4400.00, '2025-01-01');
INSERT INTO carburant_tarif (carburant_id, prix, date_tarif) VALUES (3, 5200.00, '2025-01-01');

-- Table change (1 EUR = X Ariary, 1 USD = X Ariary)
INSERT INTO change (devise_id, date_change, cours) VALUES (1, '2025-01-01', 1.0000);
INSERT INTO change (devise_id, date_change, cours) VALUES (2, '2025-01-01', 5100.0000);
INSERT INTO change (devise_id, date_change, cours) VALUES (3, '2025-01-01', 4600.0000);
