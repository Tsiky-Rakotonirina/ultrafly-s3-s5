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

--6