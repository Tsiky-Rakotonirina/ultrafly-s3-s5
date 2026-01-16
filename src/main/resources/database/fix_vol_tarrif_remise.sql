-- Correction de la table vol_tarrif_remise pour ajouter client_type_id

-- Supprimer la table existante
DROP TABLE IF EXISTS vol_tarrif_remise CASCADE;
DROP SEQUENCE IF EXISTS seq_vol_tarrif_remise CASCADE;

-- Recréer la table avec la bonne structure
CREATE SEQUENCE seq_vol_tarrif_remise START 1;    

CREATE TABLE vol_tarrif_remise(
    id_vol_tarrif_remise BIGINT PRIMARY KEY DEFAULT nextval('seq_vol_tarrif_remise'),
    vol_tarrif_id BIGINT NOT NULL,
    client_type_id BIGINT NOT NULL,
    prix NUMERIC(10,2) NOT NULL,
    date_remise DATE NOT NULL,
    FOREIGN KEY (vol_tarrif_id) REFERENCES vol_tarrif(id_vol_tarrif),
    FOREIGN KEY (client_type_id) REFERENCES client_type(id_client_type)
);

-- Réinsérer les données de remise pour Enfant
INSERT INTO vol_tarrif_remise (vol_tarrif_id, client_type_id, prix, date_remise) 
VALUES (
    (SELECT id_vol_tarrif FROM vol_tarrif WHERE vol_id = 1 AND siege_categorie_id = (SELECT id_siege_categorie FROM siege_categorie WHERE libelle = 'Economique')),
    (SELECT id_client_type FROM client_type WHERE libelle = 'Enfant'),
    500000.00,
    '2026-01-16'
);
