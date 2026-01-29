update vol_tarrif set prix = 900000 where id_vol_tarrif = 3;



SELECT sum(e.montant) as montant_total, sum(e.reste_a_payer) as reste_a_payer, s.nom_societe 
FROM encaissement e 
JOIN publicite_diffusion_vol pdv ON e.publicite_diffusion_vol_id = pdv.id_publicite_diffusion_vol 
JOIN publicite_diffusion pd ON pdv.publicite_diffusion_id = pd.id_publicite_diffusion
JOIN societe s ON pd.societe_id = s.id_societe 
GROUP BY s.id_societe, s.nom_societe;