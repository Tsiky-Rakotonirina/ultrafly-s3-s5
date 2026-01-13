CREATE TABLE avion_statut (
    id_avion_statut SERIAL PRIMARY KEY,
    libelle         VARCHAR(100) NOT NULL,
    description     TEXT
);

CREATE TABLE siege_categorie (
    id_siege_categorie SERIAL PRIMARY KEY,
    libelle            VARCHAR(100) NOT NULL,
    description        TEXT
);

CREATE TABLE carburant (
    id_carburant SERIAL PRIMARY KEY,
    libelle           VARCHAR(100) NOT NULL,
    description       TEXT
);

CREATE TABLE vol_type (
    id_vol_type SERIAL PRIMARY KEY,
    libelle     VARCHAR(100) NOT NULL,
    description TEXT
);

CREATE TABLE vol_statut (
    id_vol_statut SERIAL PRIMARY KEY,
    libelle       VARCHAR(100) NOT NULL,
    description   TEXT
);

CREATE TABLE vol_report_type (
    id_vol_report_type SERIAL PRIMARY KEY,
    libelle            VARCHAR(100) NOT NULL,
    description        TEXT
);

CREATE TABLE poste (
    id_poste    SERIAL PRIMARY KEY,
    libelle     VARCHAR(100) NOT NULL,
    description TEXT
);

CREATE TABLE role (
    id_role     SERIAL PRIMARY KEY,
    libelle     VARCHAR(100) NOT NULL,
    description TEXT
);

CREATE TABLE poste_role (
    id_poste_role SERIAL PRIMARY KEY,
    poste_id INTEGER NOT NULL,
    role_id  INTEGER NOT NULL,

    CONSTRAINT fk_poste_role_poste
        FOREIGN KEY (poste_id)
        REFERENCES poste(id_poste),

    CONSTRAINT fk_poste_role_role
        FOREIGN KEY (role_id)
        REFERENCES role(id_role)
);


CREATE TABLE client_type (
    id_client_type SERIAL PRIMARY KEY,
    libelle        VARCHAR(100) NOT NULL,
    description    TEXT
);

CREATE TABLE reservation_statut (
    id_reservation_statut SERIAL PRIMARY KEY,
    libelle               VARCHAR(100) NOT NULL,
    description           TEXT
);

CREATE TABLE billet_statut (
    id_billet_statut SERIAL PRIMARY KEY,
    libelle          VARCHAR(100) NOT NULL,
    description      TEXT
);

CREATE TABLE bagage_type (
    id_bagage_type SERIAL PRIMARY KEY,
    libelle        VARCHAR(100) NOT NULL,
    description    TEXT
);

CREATE TABLE devise (
    id_devise SERIAL PRIMARY KEY,
    libelle          VARCHAR(100) NOT NULL,
    description      TEXT
);

CREATE TABLE paiement_mode (
    id_paiement_mode SERIAL PRIMARY KEY,
    libelle          VARCHAR(100) NOT NULL,
    description      TEXT
);

CREATE TABLE pays (
    id_pays     SERIAL PRIMARY KEY,
    nom         VARCHAR(100) NOT NULL,
    nationalite VARCHAR(100)
);

CREATE TABLE ville (
    id_ville SERIAL PRIMARY KEY,
    nom      VARCHAR(100) NOT NULL,
    pays_id  INTEGER NOT NULL,
    CONSTRAINT fk_ville_pays
        FOREIGN KEY (pays_id)
        REFERENCES pays(id_pays)
);

CREATE TABLE aeroport (
    id_aeroport SERIAL PRIMARY KEY,
    nom         VARCHAR(150) NOT NULL,
    code_iata   VARCHAR(10) NOT NULL,
    ville_id    INTEGER NOT NULL,
    CONSTRAINT fk_aeroport_ville
        FOREIGN KEY (ville_id)
        REFERENCES ville(id_ville)
);

CREATE TABLE carburant_tarif (
    id_carburant_tarif   SERIAL PRIMARY KEY,
    carburant_id   INTEGER       NOT NULL,
    prix                NUMERIC(10,2) NOT NULL,
    date_tarif          DATE          NOT NULL,

    CONSTRAINT fk_tarif_carburant
        FOREIGN KEY (carburant_id)
        REFERENCES carburant(id_carburant),

    CONSTRAINT uq_carburant_date
        UNIQUE (carburant_id, date_tarif)
);


CREATE TABLE change (
    id_change   SERIAL PRIMARY KEY,
    devise_id   INTEGER NOT NULL,
    date_change DATE NOT NULL,
    cours       NUMERIC(10,4) NOT NULL,
    CONSTRAINT fk_change_devise
        FOREIGN KEY (devise_id)
        REFERENCES devise(id_devise),
    CONSTRAINT uq_devise_date
        UNIQUE (devise_id, date_change)
);

-- 19