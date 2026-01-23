update vol_tarrif set prix = 900000 where id_vol_tarrif = 3;



SELECT sum(e.montant) as montant_total, sum(e.reste_a_payer) as reste_a_payer,  s.nom_societe from encaissement e JOIN publicite_diffusion pd on e.publicite_diffusion_id = pd.id_publicite_diffusion JOIN societe s ON s.id_societe = pd.societe_id group by pd.societe_id, s.nom_societe;