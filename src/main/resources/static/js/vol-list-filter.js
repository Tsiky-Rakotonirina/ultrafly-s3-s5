/* ========================================
   VOL LIST FILTER - ULTRAFLY
   Filtres avancés pour la liste des vols
   ======================================== */

document.addEventListener('DOMContentLoaded', function() {
    initVolFilters();
});

// Variables globales pour stocker les données des vols
let allVolsData = [];
let categoriesSieges = new Set();

function initVolFilters() {
    // Collecter toutes les données des vols depuis le DOM
    collectVolsData();
    
    // Peupler les select dynamiques
    populateSelectOptions();
    
    // Attacher les événements de filtrage
    attachFilterEvents();
}

// Collecter les données des vols depuis le DOM
function collectVolsData() {
    const volCards = document.querySelectorAll('.vol-card');
    
    volCards.forEach(card => {
        // Parse avion IDs - peut être un seul ID ou une liste séparée par des virgules
        const avionIdsRaw = card.dataset.avionIds || '';
        const avionIds = avionIdsRaw ? avionIdsRaw.split(',').map(id => parseInt(id.trim())).filter(id => !isNaN(id) && id > 0) : [];
        
        const data = {
            element: card,
            numero: card.dataset.numero || '',
            dateHeure: card.dataset.dateHeure || '',
            statutVolId: parseInt(card.dataset.statutVolId) || 0,
            statutVolLibelle: card.dataset.statutVolLibelle || '',
            volTypeId: parseInt(card.dataset.volTypeId) || 0,
            volTypeLibelle: card.dataset.volTypeLibelle || '',
            itineraireId: parseInt(card.dataset.itineraireId) || 0,
            aeroportDepartId: parseInt(card.dataset.aeroportDepartId) || 0,
            aeroportDepartNom: card.dataset.aeroportDepartNom || '',
            aeroportArriveeId: parseInt(card.dataset.aeroportArriveeId) || 0,
            aeroportArriveeNom: card.dataset.aeroportArriveeNom || '',
            nombreEscales: parseInt(card.dataset.nombreEscales) || 0,
            avionIds: avionIds,
            avionNom: card.dataset.avionNom || '',
            nombreReservations: parseInt(card.dataset.nombreReservations) || 0,
            tarrifMoyen: parseFloat(card.dataset.tarrifMoyen) || 0,
            chiffreAffaire: parseFloat(card.dataset.chiffreAffaire) || 0,
            recetteMaxTotal: parseFloat(card.dataset.recetteMaxTotal) || 0,
            // Données par catégorie de siège (format JSON)
            categoriesData: JSON.parse(card.dataset.categoriesData || '[]')
        };
        
        // Collecter les catégories de sièges
        data.categoriesData.forEach(cat => {
            categoriesSieges.add(JSON.stringify({ id: cat.categorieId, libelle: cat.categorieLibelle }));
        });
        
        allVolsData.push(data);
    });
}

// Peupler les options des select dynamiquement
function populateSelectOptions() {
    // Statuts
    const statutSet = new Set();
    allVolsData.forEach(v => statutSet.add(JSON.stringify({ id: v.statutVolId, libelle: v.statutVolLibelle })));
    populateSelect('filterStatut', Array.from(statutSet).map(s => JSON.parse(s)));
    
    // Types de vol
    const typeSet = new Set();
    allVolsData.forEach(v => typeSet.add(JSON.stringify({ id: v.volTypeId, libelle: v.volTypeLibelle })));
    populateSelect('filterVolType', Array.from(typeSet).map(t => JSON.parse(t)));
    
    // Aéroports départ
    const departSet = new Set();
    allVolsData.forEach(v => departSet.add(JSON.stringify({ id: v.aeroportDepartId, libelle: v.aeroportDepartNom })));
    populateSelect('filterAeroportDepart', Array.from(departSet).map(a => JSON.parse(a)));
    
    // Aéroports arrivée
    const arriveeSet = new Set();
    allVolsData.forEach(v => arriveeSet.add(JSON.stringify({ id: v.aeroportArriveeId, libelle: v.aeroportArriveeNom })));
    populateSelect('filterAeroportArrivee', Array.from(arriveeSet).map(a => JSON.parse(a)));
    
    // Avions - utilise avionIds et avionNom du vol
    const avionSet = new Set();
    allVolsData.forEach(v => {
        if (v.avionIds.length > 0 && v.avionNom) {
            // Pour chaque avion ID, on ajoute le nom de l'avion
            v.avionIds.forEach(id => {
                avionSet.add(JSON.stringify({ id: id, libelle: v.avionNom }));
            });
        }
    });
    populateMultiSelect('filterAvions', Array.from(avionSet).map(a => JSON.parse(a)));
    
    // Catégories de sièges
    populateSelect('filterSiegeCategorie', Array.from(categoriesSieges).map(c => JSON.parse(c)));
}

function populateSelect(selectId, options) {
    const select = document.getElementById(selectId);
    if (!select) return;
    
    options.forEach(opt => {
        if (opt.id && opt.libelle) {
            const option = document.createElement('option');
            option.value = opt.id;
            option.textContent = opt.libelle;
            select.appendChild(option);
        }
    });
}

function populateMultiSelect(containerId, options) {
    const container = document.getElementById(containerId);
    if (!container) return;
    
    options.forEach(opt => {
        if (opt.id && opt.libelle) {
            const div = document.createElement('div');
            div.className = 'filter-checkbox-item';
            div.dataset.value = opt.id;
            div.innerHTML = `
                <input type="checkbox" value="${opt.id}" id="avion_${opt.id}">
                <label for="avion_${opt.id}">${opt.libelle}</label>
            `;
            container.appendChild(div);
        }
    });
}

// Attacher les événements de filtrage
function attachFilterEvents() {
    // Bouton de filtrage
    const filterBtn = document.getElementById('applyFilters');
    if (filterBtn) {
        filterBtn.addEventListener('click', applyFilters);
    }
    
    // Bouton de reset
    const resetBtn = document.getElementById('resetFilters');
    if (resetBtn) {
        resetBtn.addEventListener('click', resetFilters);
    }
    
    // Filtrage en temps réel sur les inputs (optionnel)
    const inputs = document.querySelectorAll('.vol-filter-input');
    inputs.forEach(input => {
        input.addEventListener('change', function() {
            // Auto-filter on change si on veut (décommenter)
            // applyFilters();
        });
    });
}

// Appliquer les filtres
function applyFilters() {
    const filters = getFilterValues();
    
    allVolsData.forEach(vol => {
        let visible = true;
        
        // Filtre Date Min
        if (filters.dateMin && vol.dateHeure) {
            const volDate = new Date(vol.dateHeure);
            const filterDate = new Date(filters.dateMin);
            if (volDate < filterDate) visible = false;
        }
        
        // Filtre Date Max
        if (filters.dateMax && vol.dateHeure) {
            const volDate = new Date(vol.dateHeure);
            const filterDate = new Date(filters.dateMax);
            if (volDate > filterDate) visible = false;
        }
        
        // Filtre Statut
        if (filters.statutVolId && vol.statutVolId !== parseInt(filters.statutVolId)) {
            visible = false;
        }
        
        // Filtre Type Vol
        if (filters.volTypeId && vol.volTypeId !== parseInt(filters.volTypeId)) {
            visible = false;
        }
        
        // Filtre Aéroport Départ
        if (filters.aeroportDepartId && vol.aeroportDepartId !== parseInt(filters.aeroportDepartId)) {
            visible = false;
        }
        
        // Filtre Aéroport Arrivée
        if (filters.aeroportArriveeId && vol.aeroportArriveeId !== parseInt(filters.aeroportArriveeId)) {
            visible = false;
        }
        
        // Filtre Nombre Escales Min
        if (filters.nombreEscalesMin !== '' && vol.nombreEscales < parseInt(filters.nombreEscalesMin)) {
            visible = false;
        }
        
        // Filtre Nombre Escales Max
        if (filters.nombreEscalesMax !== '' && vol.nombreEscales > parseInt(filters.nombreEscalesMax)) {
            visible = false;
        }
        
        // Filtre Avions (au moins un avion sélectionné doit correspondre)
        if (filters.avionIds.length > 0) {
            const hasMatchingAvion = vol.avionIds.some(id => filters.avionIds.includes(id));
            if (!hasMatchingAvion) visible = false;
        }
        
        // Filtre Nombre Réservations Min
        if (filters.nombreReservationsMin !== '' && vol.nombreReservations < parseInt(filters.nombreReservationsMin)) {
            visible = false;
        }
        
        // Filtre Nombre Réservations Max
        if (filters.nombreReservationsMax !== '' && vol.nombreReservations > parseInt(filters.nombreReservationsMax)) {
            visible = false;
        }
        
        // Filtre Tarif Moyen Min
        if (filters.tarrifMoyenMin !== '' && vol.tarrifMoyen < parseFloat(filters.tarrifMoyenMin)) {
            visible = false;
        }
        
        // Filtre Tarif Moyen Max
        if (filters.tarrifMoyenMax !== '' && vol.tarrifMoyen > parseFloat(filters.tarrifMoyenMax)) {
            visible = false;
        }
        
        // Filtre Chiffre d'Affaire Min
        if (filters.chiffreAffaireMin !== '' && vol.chiffreAffaire < parseFloat(filters.chiffreAffaireMin)) {
            visible = false;
        }
        
        // Filtre Chiffre d'Affaire Max
        if (filters.chiffreAffaireMax !== '' && vol.chiffreAffaire > parseFloat(filters.chiffreAffaireMax)) {
            visible = false;
        }
        
        // Filtre Recette Max Min
        if (filters.recetteMaxMin !== '' && vol.recetteMaxTotal < parseFloat(filters.recetteMaxMin)) {
            visible = false;
        }
        
        // Filtre Recette Max Max
        if (filters.recetteMaxMax !== '' && vol.recetteMaxTotal > parseFloat(filters.recetteMaxMax)) {
            visible = false;
        }
        
        // Filtres par catégorie de siège
        if (filters.siegeCategorieId) {
            const catId = parseInt(filters.siegeCategorieId);
            const catData = vol.categoriesData.find(c => c.categorieId === catId);
            
            if (catData) {
                // Prix catégorie
                if (filters.prixCategorieMin !== '' && catData.prix < parseFloat(filters.prixCategorieMin)) {
                    visible = false;
                }
                if (filters.prixCategorieMax !== '' && catData.prix > parseFloat(filters.prixCategorieMax)) {
                    visible = false;
                }
                
                // Sièges pris catégorie
                if (filters.siegesPrisMin !== '' && catData.siegesPris < parseInt(filters.siegesPrisMin)) {
                    visible = false;
                }
                if (filters.siegesPrisMax !== '' && catData.siegesPris > parseInt(filters.siegesPrisMax)) {
                    visible = false;
                }
                
                // Sièges total catégorie
                if (filters.siegesTotalMin !== '' && catData.siegesTotal < parseInt(filters.siegesTotalMin)) {
                    visible = false;
                }
                if (filters.siegesTotalMax !== '' && catData.siegesTotal > parseInt(filters.siegesTotalMax)) {
                    visible = false;
                }
                
                // Recette max catégorie
                if (filters.recetteCategorieMin !== '' && catData.recetteMax < parseFloat(filters.recetteCategorieMin)) {
                    visible = false;
                }
                if (filters.recetteCategorieMax !== '' && catData.recetteMax > parseFloat(filters.recetteCategorieMax)) {
                    visible = false;
                }
            } else {
                // Si la catégorie n'existe pas pour ce vol, ne pas l'afficher
                visible = false;
            }
        }
        
        // Afficher ou masquer le vol
        vol.element.style.display = visible ? '' : 'none';
    });
    
    // Mettre à jour le compteur de résultats
    updateResultCount();
}

// Récupérer les valeurs des filtres
function getFilterValues() {
    return {
        // Date/Heure
        dateMin: getValue('filterDateMin'),
        dateMax: getValue('filterDateMax'),
        
        // Références
        statutVolId: getValue('filterStatut'),
        volTypeId: getValue('filterVolType'),
        aeroportDepartId: getValue('filterAeroportDepart'),
        aeroportArriveeId: getValue('filterAeroportArrivee'),
        
        // Nombre escales
        nombreEscalesMin: getValue('filterEscalesMin'),
        nombreEscalesMax: getValue('filterEscalesMax'),
        
        // Avions
        avionIds: getCheckedValues('filterAvions'),
        
        // Nombre réservations
        nombreReservationsMin: getValue('filterReservationsMin'),
        nombreReservationsMax: getValue('filterReservationsMax'),
        
        // Tarif moyen
        tarrifMoyenMin: getValue('filterTarrifMin'),
        tarrifMoyenMax: getValue('filterTarrifMax'),
        
        // Chiffre d'affaire
        chiffreAffaireMin: getValue('filterCAMin'),
        chiffreAffaireMax: getValue('filterCAMax'),
        
        // Recette max totale
        recetteMaxMin: getValue('filterRecetteMin'),
        recetteMaxMax: getValue('filterRecetteMax'),
        
        // Filtres par catégorie
        siegeCategorieId: getValue('filterSiegeCategorie'),
        prixCategorieMin: getValue('filterPrixCatMin'),
        prixCategorieMax: getValue('filterPrixCatMax'),
        siegesPrisMin: getValue('filterSiegesPrisMin'),
        siegesPrisMax: getValue('filterSiegesPrisMax'),
        siegesTotalMin: getValue('filterSiegesTotalMin'),
        siegesTotalMax: getValue('filterSiegesTotalMax'),
        recetteCategorieMin: getValue('filterRecetteCatMin'),
        recetteCategorieMax: getValue('filterRecetteCatMax')
    };
}

function getValue(id) {
    const el = document.getElementById(id);
    return el ? el.value : '';
}

function getCheckedValues(containerId) {
    const container = document.getElementById(containerId);
    if (!container) return [];
    
    const checkboxes = container.querySelectorAll('input[type="checkbox"]:checked');
    return Array.from(checkboxes).map(cb => parseInt(cb.value));
}

// Réinitialiser les filtres
function resetFilters() {
    // Reset tous les inputs
    const inputs = document.querySelectorAll('#volFilters input, #volFilters select');
    inputs.forEach(input => {
        if (input.type === 'checkbox') {
            input.checked = false;
        } else {
            input.value = '';
        }
    });
    
    // Réafficher tous les vols
    allVolsData.forEach(vol => {
        vol.element.style.display = '';
    });
    
    // Mettre à jour le compteur
    updateResultCount();
}

// Mettre à jour le compteur de résultats
function updateResultCount() {
    const visibleCount = allVolsData.filter(v => v.element.style.display !== 'none').length;
    const totalCount = allVolsData.length;
    
    const countEl = document.getElementById('filterResultCount');
    if (countEl) {
        countEl.textContent = `${visibleCount} / ${totalCount} vols affichés`;
    }
}

// Toggle du panneau de filtres avancés par catégorie
function toggleCategoryFilters() {
    const panel = document.getElementById('categoryFiltersPanel');
    if (panel) {
        panel.classList.toggle('hidden');
    }
}
