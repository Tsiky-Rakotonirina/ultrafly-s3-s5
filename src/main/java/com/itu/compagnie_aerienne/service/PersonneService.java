package com.itu.compagnie_aerienne.service;

import com.itu.compagnie_aerienne.model.*;
import com.itu.compagnie_aerienne.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PersonneService {
    
    @Autowired
    private PersonneRepository personneRepository;
    
    @Autowired
    private ClientRepository clientRepository;
    
    @Autowired
    private EmployeRepository employeRepository;
    
    @Autowired
    private EquipageRepository equipageRepository;
    
    @Autowired
    private EquipageMembreRepository equipageMembreRepository;
    
    @Autowired
    private PaysRepository paysRepository;
    
    @Autowired
    private PosteRepository posteRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private ClientTypeRepository clientTypeRepository;
    
    // ==================== PERSONNE ====================
    
    public List<Personne> findAllPersonnes() {
        return personneRepository.findAll();
    }
    
    public Optional<Personne> findPersonneById(Integer id) {
        return personneRepository.findById(id);
    }
    
    public List<Personne> filtrerPersonnes(String nom, String email, Integer paysId) {
        return personneRepository.filtrer(nom, email, paysId);
    }
    
    public Personne savePersonne(Personne personne) {
        if (personne.getDatePersonne() == null) {
            personne.setDatePersonne(LocalDate.now());
        }
        return personneRepository.save(personne);
    }
    
    public void deletePersonne(Integer id) {
        personneRepository.deleteById(id);
    }
    
    // ==================== CLIENT ====================
    
    public List<Client> findAllClients() {
        return clientRepository.findAll();
    }
    
    public Optional<Client> findClientById(Integer id) {
        return clientRepository.findById(id);
    }
    
    public List<Client> filtrerClients(String nom, String passeport, Integer paysId) {
        return clientRepository.filtrer(nom, passeport, paysId);
    }
    
    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }
    
    public Client creerClient(String nom, String email, LocalDate dateNaissance, Integer paysId, String passeport) {
        // Créer la personne d'abord
        Personne personne = new Personne();
        personne.setNom(nom);
        personne.setEmail(email);
        personne.setDateNaissance(dateNaissance);
        personne.setDatePersonne(LocalDate.now());
        
        if (paysId != null) {
            personne.setPays(paysRepository.findById(paysId).orElse(null));
        }
        
        personne = personneRepository.save(personne);
        
        // Créer le client
        Client client = new Client();
        client.setPasseport(passeport);
        client.setPersonne(personne);
        
        return clientRepository.save(client);
    }
    
    public Client modifierClient(Integer clientId, String nom, String email, LocalDate dateNaissance, Integer paysId, String passeport) {
        Client client = clientRepository.findById(clientId)
            .orElseThrow(() -> new RuntimeException("Client non trouvé"));
        
        Personne personne = client.getPersonne();
        personne.setNom(nom);
        personne.setEmail(email);
        personne.setDateNaissance(dateNaissance);
        
        if (paysId != null) {
            personne.setPays(paysRepository.findById(paysId).orElse(null));
        }
        
        personneRepository.save(personne);
        
        client.setPasseport(passeport);
        
        return clientRepository.save(client);
    }
    
    public void deleteClient(Integer id) {
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Client non trouvé"));
        clientRepository.delete(client);
    }
    
    // ==================== EMPLOYE ====================
    
    public List<Employe> findAllEmployes() {
        return employeRepository.findAll();
    }
    
    public Optional<Employe> findEmployeById(Integer id) {
        return employeRepository.findById(id);
    }
    
    public List<Employe> filtrerEmployes(String nom, Integer posteId, Integer paysId) {
        return employeRepository.filtrer(nom, posteId, paysId);
    }
    
    public Employe creerEmploye(String nom, String email, LocalDate dateNaissance, Integer paysId, Integer posteId) {
        // Créer la personne d'abord
        Personne personne = new Personne();
        personne.setNom(nom);
        personne.setEmail(email);
        personne.setDateNaissance(dateNaissance);
        personne.setDatePersonne(LocalDate.now());
        
        if (paysId != null) {
            personne.setPays(paysRepository.findById(paysId).orElse(null));
        }
        
        personne = personneRepository.save(personne);
        
        // Créer l'employé
        Employe employe = new Employe();
        employe.setPersonne(personne);
        
        if (posteId != null) {
            employe.setPoste(posteRepository.findById(posteId).orElse(null));
        }
        
        return employeRepository.save(employe);
    }
    
    public Employe modifierEmploye(Integer employeId, String nom, String email, LocalDate dateNaissance, Integer paysId, Integer posteId) {
        Employe employe = employeRepository.findById(employeId)
            .orElseThrow(() -> new RuntimeException("Employé non trouvé"));
        
        Personne personne = employe.getPersonne();
        personne.setNom(nom);
        personne.setEmail(email);
        personne.setDateNaissance(dateNaissance);
        
        if (paysId != null) {
            personne.setPays(paysRepository.findById(paysId).orElse(null));
        }
        
        personneRepository.save(personne);
        
        if (posteId != null) {
            employe.setPoste(posteRepository.findById(posteId).orElse(null));
        }
        
        return employeRepository.save(employe);
    }
    
    public void deleteEmploye(Integer id) {
        Employe employe = employeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Employé non trouvé"));
        employeRepository.delete(employe);
    }
    
    // ==================== EQUIPAGE ====================
    
    public List<Equipage> findAllEquipages() {
        return equipageRepository.findAll();
    }
    
    public Optional<Equipage> findEquipageById(Integer id) {
        return equipageRepository.findById(id);
    }
    
    public List<Equipage> filtrerEquipages(String nom, LocalDate dateDebut, LocalDate dateFin) {
        return equipageRepository.filtrer(nom, dateDebut, dateFin);
    }
    
    public Equipage saveEquipage(Equipage equipage) {
        if (equipage.getDateEquipage() == null) {
            equipage.setDateEquipage(LocalDate.now());
        }
        return equipageRepository.save(equipage);
    }
    
    public void deleteEquipage(Integer id) {
        equipageRepository.deleteById(id);
    }
    
    // ==================== EQUIPAGE MEMBRE ====================
    
    public List<EquipageMembre> findMembresByEquipage(Integer equipageId) {
        return equipageMembreRepository.findByEquipageIdEquipageOrderByOrdreAsc(equipageId);
    }
    
    public EquipageMembre ajouterMembre(Integer equipageId, Integer employeId, Integer roleId, Integer ordre) {
        Equipage equipage = equipageRepository.findById(equipageId)
            .orElseThrow(() -> new RuntimeException("Équipage non trouvé"));
        Employe employe = employeRepository.findById(employeId)
            .orElseThrow(() -> new RuntimeException("Employé non trouvé"));
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new RuntimeException("Rôle non trouvé"));
        
        EquipageMembre membre = new EquipageMembre();
        membre.setEquipage(equipage);
        membre.setEmploye(employe);
        membre.setRole(role);
        membre.setOrdre(ordre);
        
        return equipageMembreRepository.save(membre);
    }
    
    public void retirerMembre(Integer membreId) {
        equipageMembreRepository.deleteById(membreId);
    }
    
    // ==================== REFERENCES ====================
    
    public List<Pays> findAllPays() {
        return paysRepository.findAll();
    }
    
    public List<Poste> findAllPostes() {
        return posteRepository.findAll();
    }
    
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }
    
    public List<ClientType> findAllClientTypes() {
        return clientTypeRepository.findAll();
    }
}
