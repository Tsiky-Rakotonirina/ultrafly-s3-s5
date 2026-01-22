update encaissement set montant = .00  where id_encaissement = 1;





update publicite_tarrif set cout = .00 where id_publicite_tarrif = 1;


INSERT INTO encaissement (societe_id, date_encaissement, montant) VALUES (
    (SELECT id_societe FROM societe WHERE nom = 'Vaniala'),
    '2025-12-15',
    2000000.00
);