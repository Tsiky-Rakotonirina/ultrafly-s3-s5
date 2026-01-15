-- Script pour réinitialiser les séquences PostgreSQL après insertion manuelle de données
-- Exécuter ce script si vous obtenez des erreurs de clé primaire dupliquée

-- Réinitialiser toutes les séquences pour qu'elles commencent après le dernier ID existant

-- Tables principales
SELECT setval('reservation_id_reservation_seq', COALESCE((SELECT MAX(id_reservation) FROM reservation), 0) + 1, false);
SELECT setval('reservation_billet_id_reservation_billet_seq', COALESCE((SELECT MAX(id_reservation_billet) FROM reservation_billet), 0) + 1, false);
SELECT setval('paiement_id_paiement_seq', COALESCE((SELECT MAX(id_paiement) FROM paiement), 0) + 1, false);
SELECT setval('paiement_detail_id_paiement_detail_seq', COALESCE((SELECT MAX(id_paiement_detail) FROM paiement_detail), 0) + 1, false);

-- Autres tables (au cas où)
SELECT setval('vol_id_vol_seq', COALESCE((SELECT MAX(id_vol) FROM vol), 0) + 1, false);
SELECT setval('client_id_client_seq', COALESCE((SELECT MAX(id_client) FROM client), 0) + 1, false);
SELECT setval('avion_siege_id_avion_siege_seq', COALESCE((SELECT MAX(id_avion_siege) FROM avion_siege), 0) + 1, false);
SELECT setval('vol_detail_id_vol_detail_seq', COALESCE((SELECT MAX(id_vol_detail) FROM vol_detail), 0) + 1, false);
SELECT setval('vol_tarrif_id_vol_tarrif_seq', COALESCE((SELECT MAX(id_vol_tarrif) FROM vol_tarrif), 0) + 1, false);
