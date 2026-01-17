// Données globales
let tarifData = {};
let tarifRemisesData = {};
let siegeCategoriesData = {};

// Initialisation
document.addEventListener('DOMContentLoaded', function() {
    initializeData();
    setupEventListeners();
});

/**
 * Initialise les données depuis le template HTML
 */
function initializeData() {
    // Récupérer les tarifs depuis le JSON caché
    const tarifDataElement = document.getElementById('tarifData');
    if (tarifDataElement && tarifDataElement.value) {
        try {
            const parsed = JSON.parse(tarifDataElement.value);
            // Convertir la map de tarifs
            Object.keys(parsed).forEach(key => {
                // La clé est un nombre (siegeCategorieId) ou le libellé
                const obj = parsed[key];
                if (obj && obj.libelle) {
                    tarifData[obj.libelle] = parseFloat(obj.prix) || 0;
                }
            });
        } catch (e) {
            console.warn('Could not parse tarif data:', e);
            // Fallback: parser depuis la table
            parseTarifFromTable();
        }
    } else {
        // Fallback: parser depuis la table
        parseTarifFromTable();
    }

    // Récupérer les remises depuis le JSON
    const tarifRemisesElement = document.getElementById('tarifRemisesData');
    if (tarifRemisesElement && tarifRemisesElement.value) {
        try {
            tarifRemisesData = JSON.parse(tarifRemisesElement.value);
        } catch (e) {
            console.warn('Could not parse remises data:', e);
        }
    }

    console.log('Tarifs loaded:', tarifData);
    console.log('Remises loaded:', tarifRemisesData);
}

/**
 * Parser les tarifs depuis la table HTML (fallback)
 */
function parseTarifFromTable() {
    const tarifTableBody = document.getElementById('tarifTableBody');
    if (tarifTableBody) {
        const rows = tarifTableBody.querySelectorAll('tr');
        rows.forEach(row => {
            const cells = row.querySelectorAll('td');
            if (cells.length >= 2) {
                const siegeCategorie = cells[0].textContent.trim();
                const tarifText = cells[1].textContent.trim();
                const tarifValue = parseFloat(tarifText.replace(/\s*FMG/, '').replace(/\s/g, ''));
                tarifData[siegeCategorie] = tarifValue;
            }
        });
    }
}

/**
 * Configure les écouteurs d'événements
 */
function setupEventListeners() {
    const calculateBtn = document.getElementById('calculateBtn');
    if (calculateBtn) {
        calculateBtn.addEventListener('click', handleCalculation);
    }

    const clientInputs = document.querySelectorAll('.clientInput');
    clientInputs.forEach(input => {
        input.addEventListener('change', () => {
            // Validation: valeur positive
            if (input.value < 0) {
                input.value = 0;
            }
        });
    });
}

/**
 * Gère le calcul du chiffre d'affaires
 */
function handleCalculation() {
    const clientInputs = document.querySelectorAll('.clientInput');
    const simulations = [];
    let totalPassengers = 0;
    let totalRevenue = 0;
    
    console.log('=== DÉBUT DU CALCUL ===');
    
    clientInputs.forEach(input => {
        const quantity = parseInt(input.value) || 0;
        
        if (quantity > 0) {
            const siegeLibelle = input.getAttribute('data-siege-libelle');
            const clientLibelle = input.getAttribute('data-client-libelle');
            const tarifBase = parseFloat(input.getAttribute('data-tarif-base')) || 0;
            
            // Récupérer les données de remise (prix fixe ou pourcentage)
            const tarifRemiseStr = input.getAttribute('data-tarif-remise');
            const pourcentageRemiseStr = input.getAttribute('data-pourcentage-remise');
            
            let prixApplique = tarifBase;
            let remiseType = 'Tarif standard';
            let pourcentageRemise = 0;
            
            // Cas 1: Prix fixe de remise existe
            if (tarifRemiseStr && tarifRemiseStr !== '' && tarifRemiseStr !== 'null') {
                prixApplique = parseFloat(tarifRemiseStr);
                pourcentageRemise = ((tarifBase - prixApplique) / tarifBase * 100).toFixed(2);
                remiseType = `Remise: ${pourcentageRemise}%`;
            }
            // Cas 2: Pourcentage de remise existe
            else if (pourcentageRemiseStr && pourcentageRemiseStr !== '' && pourcentageRemiseStr !== 'null') {
                pourcentageRemise = parseFloat(pourcentageRemiseStr);
                prixApplique = tarifBase * pourcentageRemise / 100;
                remiseType = `Remise: ${pourcentageRemise.toFixed(2)}%`;
            }
            
            console.log(`${siegeLibelle} - ${clientLibelle}:`, {
                quantity,
                tarifBase,
                tarifRemiseStr,
                pourcentageRemiseStr,
                prixApplique,
                remiseType,
                hasRemise: prixApplique < tarifBase
            });
            
            const montant = prixApplique * quantity;
            totalPassengers += quantity;
            totalRevenue += montant;

            simulations.push({
                siegeCategorie: siegeLibelle,
                clientTypeName: clientLibelle,
                quantity: quantity,
                prixApplique: prixApplique,
                remiseType: remiseType,
                montant: montant
            });
        }
    });

    console.log('Total passagers:', totalPassengers);
    console.log('Chiffre d\'affaires total:', totalRevenue);
    console.log('=== FIN DU CALCUL ===');

    if (totalPassengers === 0) {
        alert('Veuillez entrer au moins un nombre de passagers');
        return;
    }

    // Afficher les résultats
    displayResults(simulations, totalPassengers, totalRevenue);
}

/**
 * Récupère les remises depuis la table des tarifs
 */
function getRemisesFromTable() {
    // Cette fonction n'est plus nécessaire car les données sont directement dans les inputs
    return {};
}

/**
 * Affiche les résultats de la simulation
 */
function displayResults(simulations, totalPassengers, totalRevenue) {
    // Remplir la table des résultats
    const resultsTableBody = document.getElementById('resultsTableBody');
    resultsTableBody.innerHTML = '';

    simulations.forEach(sim => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${sim.siegeCategorie}</td>
            <td>${sim.clientTypeName}</td>
            <td>${sim.quantity}</td>
            <td>
                <small class="text-muted">${sim.remiseType}</small><br>
                <strong>${formatCurrency(sim.prixApplique)}</strong>
            </td>
            <td><strong>${formatCurrency(sim.montant)}</strong></td>
        `;
        resultsTableBody.appendChild(row);
    });

    // Résumé par classe
    const classBreakdown = document.getElementById('classBreakdown');
    classBreakdown.innerHTML = '';

    const classGrouped = {};
    simulations.forEach(sim => {
        if (!classGrouped[sim.siegeCategorie]) {
            classGrouped[sim.siegeCategorie] = 0;
        }
        classGrouped[sim.siegeCategorie] += sim.montant;
    });

    Object.keys(classGrouped).forEach(classe => {
        const amount = classGrouped[classe];
        const div = document.createElement('div');
        div.className = 'mb-2';
        div.innerHTML = `
            <p>
                <strong>${classe}:</strong> 
                <span class="badge bg-info">${formatCurrency(amount)}</span>
            </p>
        `;
        classBreakdown.appendChild(div);
    });

    // Résumé total
    document.getElementById('totalPassengers').textContent = totalPassengers;
    document.getElementById('totalRevenue').textContent = formatCurrency(totalRevenue);

    // Afficher la section des résultats
    document.getElementById('resultsSection').style.display = 'block';

    // Scroller vers les résultats
    document.getElementById('resultsSection').scrollIntoView({ behavior: 'smooth' });
}

/**
 * Formate un nombre en devise Ariary
 */
function formatCurrency(value) {
    if (isNaN(value)) {
        value = 0;
    }
    return Math.round(value).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ' ') + ' Ar';
}
