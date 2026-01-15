-- =====================================================
-- 1) TYPES ENUM
-- =====================================================
CREATE TYPE statut_reservation_enum AS ENUM (
    'EN_ATTENTE',
    'CONFIRMEE',
    'ANNULEA',
    'REMBOURSEE'
);

CREATE TYPE statut_billet_enum AS ENUM ('EMIS', 'ANNULE', 'UTILISE', 'REMBOURSE');

CREATE TYPE statut_paiement_enum AS ENUM ('EN_ATTENTE', 'VALIDE', 'REFUSE');

CREATE TYPE status_vol_enum AS ENUM (
    'PLANIFIE',
    'DECOLLE',
    'EN_VOL',
    'ATTERRI',
    'ANNULE',
    'RETARDE'
);

-- =====================================================
-- 2) TABLE CLASSE_SIEGE
-- =====================================================
CREATE TABLE
    classe_siege (
        id SERIAL PRIMARY KEY,
        libelle VARCHAR(50) NOT NULL UNIQUE,
        description TEXT
    );

-- =====================================================
-- 3) TABLE ROLE_EQUIPAGE
-- =====================================================
-- =====================================================
-- 4) TABLE MODEL_AVION
-- =====================================================
CREATE TABLE
    model_avion (
        id SERIAL PRIMARY KEY,
        designation VARCHAR(100) NOT NULL UNIQUE,
        fabricant VARCHAR(100) NOT NULL,
        capacite INT NOT NULL CHECK (capacite > 0),
        autonomie_km INT,
        vitesse_km_h INT,
        description TEXT
    );

-- =====================================================
-- 5) TABLE ETAT_AVION
-- =====================================================
CREATE TABLE
    etat_avion (
        id SERIAL PRIMARY KEY,
        libelle VARCHAR(50) NOT NULL UNIQUE,
        description TEXT
    );

-- =====================================================
-- 6) TABLE AVION
-- =====================================================
CREATE TABLE
    avion (
        id SERIAL PRIMARY KEY,
        code_avion VARCHAR(50) NOT NULL UNIQUE,
        model_avion_id INT NOT NULL,
        etat_avion_id INT NOT NULL,
        capacite_totale INT NOT NULL CHECK (capacite_totale > 0),
        FOREIGN KEY (model_avion_id) REFERENCES model_avion (id) ON DELETE RESTRICT,
        FOREIGN KEY (etat_avion_id) REFERENCES etat_avion (id) ON DELETE RESTRICT
    );

-- =====================================================
-- 7) TABLE SIEGE
-- =====================================================
CREATE TABLE
    siege (
        id SERIAL PRIMARY KEY,
        numero_siege VARCHAR(10) NOT NULL,
        classe_siege_id INT NOT NULL,
        avion_id INT NOT NULL,
        FOREIGN KEY (classe_siege_id) REFERENCES classe_siege (id) ON DELETE RESTRICT,
        FOREIGN KEY (avion_id) REFERENCES avion (id) ON DELETE CASCADE,
        UNIQUE (avion_id, numero_siege)
    );


-- =====================================================
-- 9) TABLE AEROPORT
-- =====================================================
CREATE TABLE
    aeroport (
        id SERIAL PRIMARY KEY,
        code_aeroport VARCHAR(10) NOT NULL UNIQUE,
        nom VARCHAR(150) NOT NULL,
        ville VARCHAR(100) NOT NULL,
        pays VARCHAR(100) NOT NULL
    );

-- =====================================================
-- 10) TABLE VOL
-- =====================================================
CREATE TABLE
    vol (
        id SERIAL PRIMARY KEY,
        numero_vol VARCHAR(50) NOT NULL UNIQUE,
        aeroport_depart_id INT NOT NULL,
        aeroport_arrivee_id INT NOT NULL,
        date_heure_depart TIMESTAMP NOT NULL,
        date_heure_arrivee TIMESTAMP NOT NULL,
        avion_id INT NOT NULL,
        status status_vol_enum DEFAULT 'PLANIFIE',
        FOREIGN KEY (aeroport_depart_id) REFERENCES aeroport (id) ON DELETE RESTRICT,
        FOREIGN KEY (aeroport_arrivee_id) REFERENCES aeroport (id) ON DELETE RESTRICT,
        FOREIGN KEY (avion_id) REFERENCES avion (id) ON DELETE RESTRICT,
        CHECK (date_heure_depart < date_heure_arrivee),
        CHECK (aeroport_depart_id != aeroport_arrivee_id)
    );

-- =====================================================
-- 11) TABLE PRIX_VOL
-- =====================================================
CREATE TABLE
    prix_vol (
        id SERIAL PRIMARY KEY,
        vol_id INT NOT NULL,
        classe_siege_id INT NOT NULL,
        prix DECIMAL(10, 2) NOT NULL CHECK (prix >= 0),
        FOREIGN KEY (vol_id) REFERENCES vol (id) ON DELETE CASCADE,
        FOREIGN KEY (classe_siege_id) REFERENCES classe_siege (id) ON DELETE RESTRICT,
        UNIQUE (vol_id, classe_siege_id)
    );

-- =====================================================
-- 12) TABLE SIEGE_VOL
-- =====================================================
CREATE TABLE
    siege_vol (
        id SERIAL PRIMARY KEY,
        vol_id INT NOT NULL,
        siege_id INT NOT NULL,
        occupe BOOLEAN DEFAULT FALSE,
        FOREIGN KEY (vol_id) REFERENCES vol (id) ON DELETE CASCADE,
        FOREIGN KEY (siege_id) REFERENCES siege (id) ON DELETE CASCADE,
        UNIQUE (vol_id, siege_id)
    );

-- =====================================================
-- 13) TABLE VOL_EQUIPAGE
-- =====================================================
-- =====================================================
-- 14) TABLE CLIENT
-- =====================================================
CREATE TABLE
    client (
        id SERIAL PRIMARY KEY,
        nom VARCHAR(100) NOT NULL,
        prenom VARCHAR(100) NOT NULL,
        email VARCHAR(100) NOT NULL UNIQUE,
        telephone VARCHAR(20)
    );

-- =====================================================
-- 15) TABLE RESERVATION
-- =====================================================
CREATE TABLE
    reservation (
        id SERIAL PRIMARY KEY,
        client_id INT NOT NULL,
        vol_id INT NOT NULL,
        date_reservation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        statut statut_reservation_enum DEFAULT 'CONFIRMEE',
        FOREIGN KEY (client_id) REFERENCES client (id) ON DELETE RESTRICT,
        FOREIGN KEY (vol_id) REFERENCES vol (id) ON DELETE RESTRICT
    );

-- =====================================================
-- 16) TABLE BILLET
-- =====================================================
CREATE TABLE
    billet (
        id SERIAL PRIMARY KEY,
        reservation_id INT NOT NULL,
        siege_vol_id INT NOT NULL,
        prix DECIMAL(10, 2) NOT NULL CHECK (prix >= 0),
        statut statut_billet_enum DEFAULT 'EMIS',
        FOREIGN KEY (reservation_id) REFERENCES reservation (id) ON DELETE CASCADE,
        FOREIGN KEY (siege_vol_id) REFERENCES siege_vol (id) ON DELETE RESTRICT
    );

-- =====================================================
-- 17) TABLE METHODE_PAIEMENT
-- =====================================================
CREATE TABLE
    methode_paiement (
        id SERIAL PRIMARY KEY,
        libelle VARCHAR(50) NOT NULL UNIQUE,
        description TEXT
    );

-- =====================================================
-- 18) TABLE PAIEMENT
-- =====================================================
CREATE TABLE
    paiement (
        id SERIAL PRIMARY KEY,
        reservation_id INT NOT NULL,
        montant DECIMAL(10, 2) NOT NULL CHECK (montant >= 0),
        date_paiement TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        statut statut_paiement_enum DEFAULT 'EN_ATTENTE',
        FOREIGN KEY (reservation_id) REFERENCES reservation (id) ON DELETE CASCADE
    );

-- =====================================================
-- 19) TABLE PAIEMENT_METHODE
-- =====================================================
CREATE TABLE
    paiement_methode (
        id SERIAL PRIMARY KEY,
        paiement_id INT NOT NULL,
        methode_paiement_id INT NOT NULL,
        montant DECIMAL(10, 2) NOT NULL CHECK (montant > 0),
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        FOREIGN KEY (paiement_id) REFERENCES paiement (id) ON DELETE CASCADE,
        FOREIGN KEY (methode_paiement_id) REFERENCES methode_paiement (id) ON DELETE RESTRICT,
        UNIQUE (paiement_id, methode_paiement_id)
    );