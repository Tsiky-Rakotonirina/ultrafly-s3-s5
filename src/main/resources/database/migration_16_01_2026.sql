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

ALTER TABLE reservation_billet ADD COLUMN client_id BIGINT;
ALTER TABLE reservation_billet ADD CONSTRAINT fk_reservation_client FOREIGN KEY (client_id) REFERENCES client(id_client);

ALTER TABLE client ADD COLUMN client_type_id BIGINT;
ALTER TABLE client ADD CONSTRAINT fk_client_client_type FOREIGN KEY (client_type_id) REFERENCES client_type(id_client_type); 

-- Mise à jour des clients existants avec des types de client
-- Client 1 : Adulte
UPDATE client SET client_type_id = (SELECT id_client_type FROM client_type WHERE libelle = 'Adulte') WHERE id_client = 1;

-- Client 2 : Enfant
UPDATE client SET client_type_id = (SELECT id_client_type FROM client_type WHERE libelle = 'Enfant') WHERE id_client = 2;

-- Client 3 : Adolescent
UPDATE client SET client_type_id = (SELECT id_client_type FROM client_type WHERE libelle = 'Adolescent') WHERE id_client = 3;

-- Client 4 : Personne âgee
UPDATE client SET client_type_id = (SELECT id_client_type FROM client_type WHERE libelle = 'Personne âgee') WHERE id_client = 4;

-- Ajout d'une remise pour la catégorie Enfant
-- Prix normal Economique: 700000.00 Ar -> Prix réduit Enfant: 500000.00 Ar
INSERT INTO vol_tarrif_remise (vol_tarrif_id, client_type_id, prix, date_remise) 
VALUES (
    (SELECT id_vol_tarrif FROM vol_tarrif WHERE vol_id = 1 AND siege_categorie_id = (SELECT id_siege_categorie FROM siege_categorie WHERE libelle = 'Economique')),
    (SELECT id_client_type FROM client_type WHERE libelle = 'Enfant'),
    500000.00,
    '2025-01-14'
);
