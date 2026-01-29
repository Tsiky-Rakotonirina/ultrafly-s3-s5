create table societe (
    id_societe serial primary key,
    nom_societe varchar(255) not null
);

create table publicite_tarrif (
    id_publicite_tarrif serial primary key,
    cout numeric(15,2) not null,
    date_tarrif date not null,
    duree_min numeric(6,2),
    duree_max numeric(6,2)
);

create table publicite_diffusion (
    id_publicite_diffusion serial primary key,
    societe_id integer references societe(id_societe),
    nombre integer not null,
    duree numeric(5,2),
    mois_annee date not null
);

create table publicite_diffusion_vol (
    id_publicite_diffusion_vol serial primary key,
    vol_id bigint references vol(id_vol),
    publicite_diffusion_id integer references publicite_diffusion(id_publicite_diffusion),
    nombre integer not null
);

create table encaissement (
    id_encaissement serial primary key,
    publicite_diffusion_vol_id integer references publicite_diffusion_vol(id_publicite_diffusion_vol),
    date_encaissement date not null,
    montant numeric(15,2) not null,
    reste_a_payer numeric(15,2) not null
);

create table encaissement_detail (
    id_encaissement_detail serial primary key,
    encaissement_id integer references encaissement(id_encaissement),
    montant numeric(15,2) not null,
    date date not null
);

INSERT INTO publicite_tarrif (cout, date_tarrif, duree_min, duree_max) VALUES (400000.00, '2025-01-01', 0.00, 1000.00);

INSERT INTO societe (nom_societe) VALUES ('Vaniala');
INSERT INTO societe (nom_societe) VALUES ('Lewis');
INSERT INTO societe (nom_societe) VALUES ('Socobis');
INSERT INTO societe (nom_societe) VALUES ('Jejoo');

INSERT INTO publicite_diffusion (societe_id, nombre, duree, mois_annee) VALUES (1, 1, 2.0, '2026-01-01');
INSERT INTO publicite_diffusion (societe_id, nombre, duree, mois_annee) VALUES (2, 1, 2.0, '2026-01-01');
INSERT INTO publicite_diffusion (societe_id, nombre, duree, mois_annee) VALUES (3, 2, 2.0, '2026-01-01');
INSERT INTO publicite_diffusion (societe_id, nombre, duree, mois_annee) VALUES (4, 1, 2.0, '2026-01-01');

INSERT INTO publicite_diffusion_vol (vol_id, publicite_diffusion_id, nombre) VALUES (1, 1, 1);
INSERT INTO publicite_diffusion_vol (vol_id, publicite_diffusion_id, nombre) VALUES (1, 2, 1);
INSERT INTO publicite_diffusion_vol (vol_id, publicite_diffusion_id, nombre) VALUES (2, 3, 2);
INSERT INTO publicite_diffusion_vol (vol_id, publicite_diffusion_id, nombre) VALUES (2, 4, 1);

-- Données de test pour les encaissements (CA publicités)
-- publicite_diffusion_vol_id 1 (vol 1, société Vaniala) : 1 000 000 Ar de montant, 200 000 Ar reste à payer => CA = 800 000 Ar
INSERT INTO encaissement (publicite_diffusion_vol_id, date_encaissement, montant, reste_a_payer) 
VALUES (1, '2026-01-15', 400000.00, 400000.00);

-- publicite_diffusion_vol_id 2 (vol 1, société Lewis) : 1 500 000 Ar de montant, 0 Ar reste à payer => CA = 1 500 000 Ar
INSERT INTO encaissement (publicite_diffusion_vol_id, date_encaissement, montant, reste_a_payer) 
VALUES (2, '2026-01-16', 400000.00, 400000.00);

-- publicite_diffusion_vol_id 3 (vol 2, société Socobis) : 2 000 000 Ar de montant, 500 000 Ar reste à payer => CA = 1 500 000 Ar
INSERT INTO encaissement (publicite_diffusion_vol_id, date_encaissement, montant, reste_a_payer) 
VALUES (3, '2026-01-17', 800000.00, 800000.00);

-- publicite_diffusion_vol_id 4 (vol 2, société Jejoo) : 800 000 Ar de montant, 100 000 Ar reste à payer => CA = 700 000 Ar
INSERT INTO encaissement (publicite_diffusion_vol_id, date_encaissement, montant, reste_a_payer) 
VALUES (4, '2026-01-17', 400000.00, 400000.00);

-- UPDATE publicite_diffusion SET nombre = 1 WHERE id_publicite_diffusion = 1;

-- INSERT INTO publicite_diffusion_vol (vol_id, publicite_diffusion_id, nombre) VALUES (3, 1, 1 );

-- INSERT INTO encaissement (publicite_diffusion_vol_id, date_encaissement, montant, reste_a_payer) 
-- VALUES (5, '2026-01-17', 400000.00, 400000.00);