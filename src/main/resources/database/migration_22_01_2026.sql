create table publicite_type (
    id_publicite_type SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL,
    duree_min INTEGER NOT NULL,
    duree_max INTEGER
);

create table societe (
    id_societe SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    adresse VARCHAR(255)
);

create SEQUENCE seq_publicite_tarrif START 1;

create table publicite_tarrif (
    id_publicite_tarrif INTEGER PRIMARY KEY DEFAULT nextval('seq_publicite_tarrif'),
    cout NUMERIC(10,2) NOT NULL,
    publicite_type_id INTEGER,
    date_tarrif DATE NOT NULL,
    FOREIGN KEY (publicite_type_id) REFERENCES publicite_type(id_publicite_type)
);

create Sequence seq_publicite_diffusion START 1;

create table publicite_diffusion (
    id_publicite_diffusion INTEGER PRIMARY KEY DEFAULT nextval('seq_publicite_diffusion'),
    societe_id INTEGER NOT NULL,
    mois_annee DATE NOT NULL,
    nombre INTEGER NOT NULL,
    duree NUMERIC(10,2),
    FOREIGN KEY (societe_id) REFERENCES societe(id_societe)
);


-- Donnees
INSERT INTO publicite_type (libelle, duree_min) VALUES ('Videos', 0);

INSERT INTO societe (nom, adresse) VALUES ('Vaniala', 'Antananarivo');
INSERT INTO societe (nom, adresse) VALUES ('Lewis', 'Antananarivo');

INSERT INTO publicite_tarrif (cout, publicite_type_id, date_tarrif) VALUES (
    400000.00, 
    (SELECT id_publicite_type FROM publicite_type WHERE libelle = 'Videos'),
    '2025-01-01'
);

INSERT INTO publicite_diffusion (societe_id, mois_annee, nombre) VALUES (
    (SELECT id_societe FROM societe WHERE nom = 'Vaniala'),
    '2025-12-01',
    20
);

INSERT INTO publicite_diffusion (societe_id, mois_annee, nombre) VALUES (
    (SELECT id_societe FROM societe WHERE nom = 'Lewis'),
    '2025-12-01',
    10
);
update vol_tarrif set prix = 900000 where id_vol_tarrif = 3;


create table encaissement (
    id_encaissement SERIAL PRIMARY KEY,
    societe_id INTEGER NOT NULL,
    date_encaissement DATE NOT NULL,
    montant NUMERIC(10,2) NOT NULL,
    FOREIGN KEY (societe_id) REFERENCES societe(id_societe)
);

INSERT INTO encaissement (societe_id, date_encaissement, montant) VALUES (
    (SELECT id_societe FROM societe WHERE nom = 'Vaniala'),
    '2025-12-15',
    1000000.00
);
