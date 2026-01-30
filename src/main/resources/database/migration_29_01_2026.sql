create or replace table produit (
    id_produit serial primary key,
    nom_produit varchar(255) not null,
    prix_unitaire numeric(15,2) not null
);

create or replace table produit_vente(
    id_produit_vente serial primary key,
    date_vente date
);

create or replace table produit_vente_detail (
    id_produit_vente_detail serial primary key,
    produit_vente_id integer references produit_vente(id_produit_vente),
    produit_id integer references produit(id_produit),
    quantite integer not null
);

INSERT INTO produit (nom_produit, prix_unitaire) VALUES
('Tablette de chocolat', 5000.00);