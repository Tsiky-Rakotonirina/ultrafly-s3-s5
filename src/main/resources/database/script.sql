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
    id_carburant_tarif SERIAL PRIMARY KEY,
    carburant_id       INTEGER       NOT NULL,
    prix               NUMERIC(10,2) NOT NULL,
    date_tarif         DATE          NOT NULL,

    CONSTRAINT fk_tarif_carburant
        FOREIGN KEY (carburant_id)
        REFERENCES carburant(id_carburant),

    CONSTRAINT uq_carburant_date
        UNIQUE (carburant_id, date_tarif)
);


CREATE TABLE change (
    id_change   SERIAL PRIMARY KEY,
    devise_id   INTEGER       NOT NULL,
    date_change DATE          NOT NULL,
    cours       NUMERIC(10,4) NOT NULL,
    CONSTRAINT fk_change_devise
        FOREIGN KEY (devise_id)
        REFERENCES devise(id_devise),
    CONSTRAINT uq_devise_date
        UNIQUE (devise_id, date_change)
);

-- Sequence pour id
CREATE SEQUENCE seq_avion START 1;

-- Table
CREATE TABLE avion (
    id_avion        INTEGER       PRIMARY KEY DEFAULT nextval('seq_avion'),
    numero          VARCHAR(10)   UNIQUE,
    modele          VARCHAR(100)  NOT NULL,
    constructeur    VARCHAR(100),
    consommation    NUMERIC(10,2),
    vitesse         NUMERIC(10,2),
    capacite        INTEGER,
    date_possession DATE,
    carburant_id    INTEGER,

    CONSTRAINT fk_avion_carburant
        FOREIGN KEY (carburant_id)
        REFERENCES carburant(id_carburant)
);

-- Fonction pour générer numero
CREATE OR REPLACE FUNCTION fn_numero_avion() RETURNS TRIGGER AS $$
BEGIN
    NEW.numero := 'AVN' || LPAD(NEW.id_avion::TEXT, 3, '0');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger
CREATE TRIGGER trg_numero_avion
BEFORE INSERT ON avion
FOR EACH ROW
EXECUTE FUNCTION fn_numero_avion();


CREATE SEQUENCE seq_avion_siege START 1;

CREATE TABLE avion_siege (
    id_avion_siege     INTEGER       PRIMARY KEY DEFAULT nextval('seq_avion_siege'),
    numero             VARCHAR(10)   UNIQUE,
    colonne            CHAR(1)       NOT NULL,
    rangee             INTEGER       NOT NULL,
    siege_categorie_id INTEGER       NOT NULL,
    avion_id           INTEGER       NOT NULL,

    CONSTRAINT fk_siege_categorie
        FOREIGN KEY (siege_categorie_id)
        REFERENCES siege_categorie(id_siege_categorie),
    CONSTRAINT fk_siege_avion
        FOREIGN KEY (avion_id)
        REFERENCES avion(id_avion)
);

CREATE OR REPLACE FUNCTION fn_numero_avion_siege() RETURNS TRIGGER AS $$
BEGIN
    NEW.numero := 'SIG' || LPAD(NEW.id_avion_siege::TEXT, 3, '0');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_numero_avion_siege
BEFORE INSERT ON avion_siege
FOR EACH ROW
EXECUTE FUNCTION fn_numero_avion_siege();


-- Sequence
CREATE SEQUENCE seq_itineraire START 1;

-- Table
CREATE TABLE itineraire (
    id_itineraire      INTEGER       PRIMARY KEY DEFAULT nextval('seq_itineraire'),
    numero             VARCHAR(10)   UNIQUE,
    duree              INTERVAL,
    distance           NUMERIC(10,2),
    aeroport_depart_id INTEGER       NOT NULL,
    aeroport_arrive_id INTEGER       NOT NULL,

    CONSTRAINT fk_itineraire_aeroport_depart
        FOREIGN KEY (aeroport_depart_id)
        REFERENCES aeroport(id_aeroport),
    CONSTRAINT fk_itineraire_aeroport_arrive
        FOREIGN KEY (aeroport_arrive_id)
        REFERENCES aeroport(id_aeroport)
);

-- Fonction pour générer numero
CREATE OR REPLACE FUNCTION fn_numero_itineraire() RETURNS TRIGGER AS $$
BEGIN
    NEW.numero := 'ITN' || LPAD(NEW.id_itineraire::TEXT, 3, '0');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger
CREATE TRIGGER trg_numero_itineraire
BEFORE INSERT ON itineraire
FOR EACH ROW
EXECUTE FUNCTION fn_numero_itineraire();


-- Sequence
CREATE SEQUENCE seq_itineraire_escale START 1;

-- Table
CREATE TABLE itineraire_escale (
    id_itineraire_escale INTEGER       PRIMARY KEY DEFAULT nextval('seq_itineraire_escale'),
    numero               VARCHAR(10)   UNIQUE,
    duree                INTERVAL,
    distance             NUMERIC(10,2),
    aeroport_depart_id   INTEGER       NOT NULL,
    aeroport_arrive_id   INTEGER       NOT NULL,
    itineraire_id        INTEGER       NOT NULL,

    CONSTRAINT fk_escale_aeroport_depart
        FOREIGN KEY (aeroport_depart_id)
        REFERENCES aeroport(id_aeroport),
    CONSTRAINT fk_escale_aeroport_arrive
        FOREIGN KEY (aeroport_arrive_id)
        REFERENCES aeroport(id_aeroport),
    CONSTRAINT fk_escale_itineraire
        FOREIGN KEY (itineraire_id)
        REFERENCES itineraire(id_itineraire)
);

-- Fonction pour générer numero
CREATE OR REPLACE FUNCTION fn_numero_itineraire_escale() RETURNS TRIGGER AS $$
BEGIN
    NEW.numero := 'ESC' || LPAD(NEW.id_itineraire_escale::TEXT, 3, '0');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger
CREATE TRIGGER trg_numero_itineraire_escale
BEFORE INSERT ON itineraire_escale
FOR EACH ROW
EXECUTE FUNCTION fn_numero_itineraire_escale();


CREATE SEQUENCE seq_personne START 1;

CREATE TABLE personne (
    id_personne    INTEGER       PRIMARY KEY DEFAULT nextval('seq_personne'),
    numero         VARCHAR(10)   UNIQUE,
    nom            VARCHAR(150)  NOT NULL,
    email          VARCHAR(150),
    date_naissance DATE,
    date_personne  DATE,
    pays_id        INTEGER,

    CONSTRAINT fk_personne_pays
        FOREIGN KEY (pays_id)
        REFERENCES pays(id_pays)
);

CREATE OR REPLACE FUNCTION fn_numero_personne() RETURNS TRIGGER AS $$
BEGIN
    NEW.numero := 'PRS' || LPAD(NEW.id_personne::TEXT, 3, '0');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_numero_personne
BEFORE INSERT ON personne
FOR EACH ROW
EXECUTE FUNCTION fn_numero_personne();


CREATE SEQUENCE seq_client START 1;

CREATE TABLE client (
    id_client   INTEGER       PRIMARY KEY DEFAULT nextval('seq_client'),
    numero      VARCHAR(10)   UNIQUE,
    passeport   VARCHAR(50),
    personne_id INTEGER       NOT NULL,

    CONSTRAINT fk_client_personne
        FOREIGN KEY (personne_id)
        REFERENCES personne(id_personne)
);

CREATE OR REPLACE FUNCTION fn_numero_client() RETURNS TRIGGER AS $$
BEGIN
    NEW.numero := 'CLI' || LPAD(NEW.id_client::TEXT, 3, '0');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_numero_client
BEFORE INSERT ON client
FOR EACH ROW
EXECUTE FUNCTION fn_numero_client();


CREATE SEQUENCE seq_employe START 1;

CREATE TABLE employe (
    id_employe  INTEGER       PRIMARY KEY DEFAULT nextval('seq_employe'),
    numero      VARCHAR(10)   UNIQUE,
    poste_id    INTEGER       NOT NULL,
    personne_id INTEGER       NOT NULL,

    CONSTRAINT fk_employe_poste
        FOREIGN KEY (poste_id)
        REFERENCES poste(id_poste),
    CONSTRAINT fk_employe_personne
        FOREIGN KEY (personne_id)
        REFERENCES personne(id_personne)
);

CREATE OR REPLACE FUNCTION fn_numero_employe() RETURNS TRIGGER AS $$
BEGIN
    NEW.numero := 'EMP' || LPAD(NEW.id_employe::TEXT, 3, '0');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_numero_employe
BEFORE INSERT ON employe
FOR EACH ROW
EXECUTE FUNCTION fn_numero_employe();


CREATE SEQUENCE seq_equipage START 1;

CREATE TABLE equipage (
    id_equipage   INTEGER       PRIMARY KEY DEFAULT nextval('seq_equipage'),
    numero        VARCHAR(10)   UNIQUE,
    nom           VARCHAR(150)  NOT NULL,
    date_equipage DATE
);

CREATE OR REPLACE FUNCTION fn_numero_equipage() RETURNS TRIGGER AS $$
BEGIN
    NEW.numero := 'EQP' || LPAD(NEW.id_equipage::TEXT, 3, '0');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_numero_equipage
BEFORE INSERT ON equipage
FOR EACH ROW
EXECUTE FUNCTION fn_numero_equipage();


CREATE SEQUENCE seq_equipage_membre START 1;

CREATE TABLE equipage_membre (
    id_equipage_membre INTEGER       PRIMARY KEY DEFAULT nextval('seq_equipage_membre'),
    numero             VARCHAR(10)   UNIQUE,
    ordre              INTEGER       NOT NULL,
    equipage_id        INTEGER       NOT NULL,
    employe_id         INTEGER       NOT NULL,
    role_id            INTEGER       NOT NULL,

    CONSTRAINT fk_membre_equipage
        FOREIGN KEY (equipage_id)
        REFERENCES equipage(id_equipage),
    CONSTRAINT fk_membre_employe
        FOREIGN KEY (employe_id)
        REFERENCES employe(id_employe),
    CONSTRAINT fk_membre_role
        FOREIGN KEY (role_id)
        REFERENCES role(id_role)
);

CREATE OR REPLACE FUNCTION fn_numero_equipage_membre() RETURNS TRIGGER AS $$
BEGIN
    NEW.numero := 'MBR' || LPAD(NEW.id_equipage_membre::TEXT, 3, '0');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_numero_equipage_membre
BEFORE INSERT ON equipage_membre
FOR EACH ROW
EXECUTE FUNCTION fn_numero_equipage_membre();



CREATE SEQUENCE seq_vol START 1;

CREATE TABLE vol (
    id_vol        INTEGER       PRIMARY KEY DEFAULT nextval('seq_vol'),
    numero        VARCHAR(10)   UNIQUE,
    heure         TIMESTAMP     NOT NULL,
    vol_type_id   INTEGER       NOT NULL,
    itineraire_id INTEGER       NOT NULL,
    statut_vol_id INTEGER       NOT NULL,

    CONSTRAINT fk_vol_type
        FOREIGN KEY (vol_type_id)
        REFERENCES vol_type(id_vol_type),
    CONSTRAINT fk_vol_itineraire
        FOREIGN KEY (itineraire_id)
        REFERENCES itineraire(id_itineraire),
    CONSTRAINT fk_vol_statut
        FOREIGN KEY (statut_vol_id)
        REFERENCES vol_statut(id_vol_statut)
);

CREATE OR REPLACE FUNCTION fn_numero_vol() RETURNS TRIGGER AS $$
BEGIN
    NEW.numero := 'VOL' || LPAD(NEW.id_vol::TEXT, 3, '0');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_numero_vol
BEFORE INSERT ON vol
FOR EACH ROW
EXECUTE FUNCTION fn_numero_vol();


CREATE SEQUENCE seq_vol_detail START 1;

CREATE TABLE vol_detail (
    id_vol_detail        INTEGER       PRIMARY KEY DEFAULT nextval('seq_vol_detail'),
    heure                TIMESTAMP     NOT NULL,
    vol_id               INTEGER       NOT NULL,
    itineraire_escale_id INTEGER,
    avion_id             INTEGER,
    equipage_id          INTEGER,

    CONSTRAINT fk_vol_detail_vol
        FOREIGN KEY (vol_id)
        REFERENCES vol(id_vol),
    CONSTRAINT fk_vol_detail_escale
        FOREIGN KEY (itineraire_escale_id)
        REFERENCES itineraire_escale(id_itineraire_escale),
    CONSTRAINT fk_vol_detail_avion
        FOREIGN KEY (avion_id)
        REFERENCES avion(id_avion),
    CONSTRAINT fk_vol_detail_equipage
        FOREIGN KEY (equipage_id)
        REFERENCES equipage(id_equipage)
);


CREATE SEQUENCE seq_vol_tarrif START 1;

CREATE TABLE vol_tarrif (
    id_vol_tarrif      INTEGER       PRIMARY KEY DEFAULT nextval('seq_vol_tarrif'),
    numero             VARCHAR(10)   UNIQUE,
    prix               NUMERIC(10,2) NOT NULL,
    siege_categorie_id INTEGER       NOT NULL,
    vol_id             INTEGER       NOT NULL,

    CONSTRAINT fk_tarrif_siege
        FOREIGN KEY (siege_categorie_id)
        REFERENCES siege_categorie(id_siege_categorie),
    CONSTRAINT fk_tarrif_vol
        FOREIGN KEY (vol_id)
        REFERENCES vol(id_vol)
);

CREATE OR REPLACE FUNCTION fn_numero_vol_tarrif() RETURNS TRIGGER AS $$
BEGIN
    NEW.numero := 'TRF' || LPAD(NEW.id_vol_tarrif::TEXT, 3, '0');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_numero_vol_tarrif
BEFORE INSERT ON vol_tarrif
FOR EACH ROW
EXECUTE FUNCTION fn_numero_vol_tarrif();


CREATE SEQUENCE seq_vol_report START 1;

CREATE TABLE vol_report (
    id_vol_report      INTEGER     PRIMARY KEY DEFAULT nextval('seq_vol_report'),
    numero             VARCHAR(10) UNIQUE,
    heure              TIMESTAMP   NOT NULL,
    vol_report_type_id INTEGER     NOT NULL,
    vol_detail_id      INTEGER     NOT NULL,

    CONSTRAINT fk_vol_report_type
        FOREIGN KEY (vol_report_type_id)
        REFERENCES vol_report_type(id_vol_report_type),
    CONSTRAINT fk_vol_report_detail
        FOREIGN KEY (vol_detail_id)
        REFERENCES vol_detail(id_vol_detail)
);

CREATE OR REPLACE FUNCTION fn_numero_vol_report() RETURNS TRIGGER AS $$
BEGIN
    NEW.numero := 'RPT' || LPAD(NEW.id_vol_report::TEXT, 3, '0');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_numero_vol_report
BEFORE INSERT ON vol_report
FOR EACH ROW
EXECUTE FUNCTION fn_numero_vol_report();


CREATE SEQUENCE seq_vol_arret START 1;

CREATE TABLE vol_arret (
    id_vol_arret INTEGER     PRIMARY KEY DEFAULT nextval('seq_vol_arret'),
    numero       VARCHAR(10) UNIQUE,
    heure        TIMESTAMP   NOT NULL,
    aeroport_id  INTEGER     NOT NULL,
    vol_id       INTEGER     NOT NULL,

    CONSTRAINT fk_arret_aeroport
        FOREIGN KEY (aeroport_id)
        REFERENCES aeroport(id_aeroport),
    CONSTRAINT fk_arret_vol
        FOREIGN KEY (vol_id)
        REFERENCES vol(id_vol)
);

CREATE OR REPLACE FUNCTION fn_numero_vol_arret() RETURNS TRIGGER AS $$
BEGIN
    NEW.numero := 'ART' || LPAD(NEW.id_vol_arret::TEXT, 3, '0');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_numero_vol_arret
BEFORE INSERT ON vol_arret
FOR EACH ROW
EXECUTE FUNCTION fn_numero_vol_arret();


CREATE SEQUENCE seq_reservation START 1;

CREATE TABLE reservation (
    id_reservation        INTEGER       PRIMARY KEY DEFAULT nextval('seq_reservation'),
    numero                VARCHAR(10)   UNIQUE,
    date_reservation      DATE          NOT NULL,
    client_id             INTEGER       NOT NULL,
    vol_id                INTEGER       NOT NULL,
    reservation_statut_id INTEGER       NOT NULL,

    CONSTRAINT fk_reservation_client
        FOREIGN KEY (client_id)
        REFERENCES client(id_client),
    CONSTRAINT fk_reservation_vol
        FOREIGN KEY (vol_id)
        REFERENCES vol(id_vol),
    CONSTRAINT fk_reservation_statut
        FOREIGN KEY (reservation_statut_id)
        REFERENCES reservation_statut(id_reservation_statut)
);

CREATE OR REPLACE FUNCTION fn_numero_reservation() RETURNS TRIGGER AS $$
BEGIN
    NEW.numero := 'RES' || LPAD(NEW.id_reservation::TEXT, 3, '0');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_numero_reservation
BEFORE INSERT ON reservation
FOR EACH ROW
EXECUTE FUNCTION fn_numero_reservation();


CREATE SEQUENCE seq_reservation_billet START 1;

CREATE TABLE reservation_billet (
    id_reservation_billet INTEGER       PRIMARY KEY DEFAULT nextval('seq_reservation_billet'),
    numero                VARCHAR(10)   UNIQUE,
    prix                  NUMERIC(10,2) NOT NULL,
    avion_siege_id        INTEGER       NOT NULL,
    reservation_id        INTEGER       NOT NULL,
    billet_statut_id      INTEGER       NOT NULL,

    CONSTRAINT fk_billet_siege
        FOREIGN KEY (avion_siege_id)
        REFERENCES avion_siege(id_avion_siege),
    CONSTRAINT fk_billet_reservation
        FOREIGN KEY (reservation_id)
        REFERENCES reservation(id_reservation),
    CONSTRAINT fk_billet_statut
        FOREIGN KEY (billet_statut_id)
        REFERENCES billet_statut(id_billet_statut)
);

CREATE OR REPLACE FUNCTION fn_numero_reservation_billet() RETURNS TRIGGER AS $$
BEGIN
    NEW.numero := 'BIL' || LPAD(NEW.id_reservation_billet::TEXT, 3, '0');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_numero_reservation_billet
BEFORE INSERT ON reservation_billet
FOR EACH ROW
EXECUTE FUNCTION fn_numero_reservation_billet();


CREATE SEQUENCE seq_enregistrement START 1;

CREATE TABLE enregistrement (
    id_enregistrement     INTEGER     PRIMARY KEY DEFAULT nextval('seq_enregistrement'),
    numero                VARCHAR(10) UNIQUE,
    heure                 TIMESTAMP   NOT NULL,
    client_type_id        INTEGER     NOT NULL,
    reservation_billet_id INTEGER     NOT NULL,

    CONSTRAINT fk_enregistrement_client_type
        FOREIGN KEY (client_type_id)
        REFERENCES client_type(id_client_type),
    CONSTRAINT fk_enregistrement_billet
        FOREIGN KEY (reservation_billet_id)
        REFERENCES reservation_billet(id_reservation_billet)
);

CREATE OR REPLACE FUNCTION fn_numero_enregistrement() RETURNS TRIGGER AS $$
BEGIN
    NEW.numero := 'ERG' || LPAD(NEW.id_enregistrement::TEXT, 3, '0');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_numero_enregistrement
BEFORE INSERT ON enregistrement
FOR EACH ROW
EXECUTE FUNCTION fn_numero_enregistrement();


CREATE SEQUENCE seq_enregistrement_bagage START 1;

CREATE TABLE enregistrement_bagage (
    id_enregistrement_bagage INTEGER       PRIMARY KEY DEFAULT nextval('seq_enregistrement_bagage'),
    numero                   VARCHAR(10)   UNIQUE,
    poids                    NUMERIC(10,2) NOT NULL,
    bagage_type_id           INTEGER       NOT NULL,
    enregistrement_id        INTEGER       NOT NULL,

    CONSTRAINT fk_bagage_type
        FOREIGN KEY (bagage_type_id)
        REFERENCES bagage_type(id_bagage_type),
    CONSTRAINT fk_bagage_enregistrement
        FOREIGN KEY (enregistrement_id)
        REFERENCES enregistrement(id_enregistrement)
);

CREATE OR REPLACE FUNCTION fn_numero_enregistrement_bagage() RETURNS TRIGGER AS $$
BEGIN
    NEW.numero := 'BAG' || LPAD(NEW.id_enregistrement_bagage::TEXT, 3, '0');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_numero_enregistrement_bagage
BEFORE INSERT ON enregistrement_bagage
FOR EACH ROW
EXECUTE FUNCTION fn_numero_enregistrement_bagage();


CREATE SEQUENCE seq_paiement START 1;

CREATE TABLE paiement (
    id_paiement       INTEGER       PRIMARY KEY DEFAULT nextval('seq_paiement'),
    numero            VARCHAR(10)   UNIQUE,
    montant_total     NUMERIC(10,2) NOT NULL,
    reste_payer       NUMERIC(10,2) NOT NULL,
    reservation_id    INTEGER,
    enregistrement_id INTEGER,

    CONSTRAINT fk_paiement_reservation
        FOREIGN KEY (reservation_id)
        REFERENCES reservation(id_reservation),
    CONSTRAINT fk_paiement_enregistrement
        FOREIGN KEY (enregistrement_id)
        REFERENCES enregistrement(id_enregistrement)
);

CREATE OR REPLACE FUNCTION fn_numero_paiement() RETURNS TRIGGER AS $$
BEGIN
    NEW.numero := 'PAI' || LPAD(NEW.id_paiement::TEXT, 3, '0');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_numero_paiement
BEFORE INSERT ON paiement
FOR EACH ROW
EXECUTE FUNCTION fn_numero_paiement();

CREATE SEQUENCE seq_avion_historique START 1;

CREATE TABLE avion_historique (
    id_avion_historique INTEGER PRIMARY KEY DEFAULT nextval('seq_avion_historique'),
    date_statut         DATE    NOT NULL,
    avion_id            INTEGER NOT NULL,
    avion_statut_id     INTEGER NOT NULL,

    CONSTRAINT fk_historique_avion
        FOREIGN KEY (avion_id)
        REFERENCES avion(id_avion),
    CONSTRAINT fk_historique_statut
        FOREIGN KEY (avion_statut_id)
        REFERENCES avion_statut(id_avion_statut)
);

CREATE SEQUENCE seq_avion_carburant START 1;

CREATE TABLE avion_carburant (
    id_avion_carburant INTEGER       PRIMARY KEY DEFAULT nextval('seq_avion_carburant'),
    quantite           NUMERIC(10,2) NOT NULL,
    date_carburant     DATE          NOT NULL,
    avion_id           INTEGER       NOT NULL,
    carburant_id       INTEGER       NOT NULL,

    CONSTRAINT fk_carburant_avion
        FOREIGN KEY (avion_id)
        REFERENCES avion(id_avion),
    CONSTRAINT fk_carburant
        FOREIGN KEY (carburant_id)
        REFERENCES carburant(id_carburant)
);

CREATE SEQUENCE seq_vol_historique START 1;

CREATE TABLE vol_historique (
    id_vol_historique INTEGER PRIMARY KEY DEFAULT nextval('seq_vol_historique'),
    date_statut       DATE    NOT NULL,
    vol_id            INTEGER NOT NULL,
    statut_vol_id     INTEGER NOT NULL,

    CONSTRAINT fk_vol_historique_vol
        FOREIGN KEY (vol_id)
        REFERENCES vol(id_vol),
    CONSTRAINT fk_vol_historique_statut
        FOREIGN KEY (statut_vol_id)
        REFERENCES vol_statut(id_vol_statut)
);

CREATE SEQUENCE seq_reservation_historique START 1;

CREATE TABLE reservation_historique (
    id_reservation_historique INTEGER PRIMARY KEY DEFAULT nextval('seq_reservation_historique'),
    date_statut               DATE    NOT NULL,
    reservation_id            INTEGER NOT NULL,
    reservation_statut_id     INTEGER NOT NULL,

    CONSTRAINT fk_reservation_historique_reservation
        FOREIGN KEY (reservation_id)
        REFERENCES reservation(id_reservation),
    CONSTRAINT fk_reservation_historique_statut
        FOREIGN KEY (reservation_statut_id)
        REFERENCES reservation_statut(id_reservation_statut)
);

CREATE SEQUENCE seq_reservation_billet_historique START 1;

CREATE TABLE reservation_billet_historique (
    id_reservation_billet_historique INTEGER PRIMARY KEY DEFAULT nextval('seq_reservation_billet_historique'),
    date_statut                      DATE    NOT NULL,
    reservation_billet_id            INTEGER NOT NULL,
    billet_statut_id                 INTEGER NOT NULL,

    CONSTRAINT fk_billet_historique_billet
        FOREIGN KEY (reservation_billet_id)
        REFERENCES reservation_billet(id_reservation_billet),
    CONSTRAINT fk_billet_historique_statut
        FOREIGN KEY (billet_statut_id)
        REFERENCES billet_statut(id_billet_statut)
);

CREATE SEQUENCE seq_paiement_detail START 1;

CREATE TABLE paiement_detail (
    id_paiement_detail INTEGER       PRIMARY KEY DEFAULT nextval('seq_paiement_detail'),
    montant            NUMERIC(10,2) NOT NULL,
    date_paiement      DATE          NOT NULL,
    paiement_mode_id   INTEGER       NOT NULL,
    devise_id          INTEGER       NOT NULL,
    paiement_id        INTEGER       NOT NULL,

    CONSTRAINT fk_paiement_detail_mode
        FOREIGN KEY (paiement_mode_id)
        REFERENCES paiement_mode(id_paiement_mode),
    CONSTRAINT fk_paiement_detail_devise
        FOREIGN KEY (devise_id)
        REFERENCES devise(id_devise),
    CONSTRAINT fk_paiement_detail_paiement
        FOREIGN KEY (paiement_id)
        REFERENCES paiement(id_paiement)
);
