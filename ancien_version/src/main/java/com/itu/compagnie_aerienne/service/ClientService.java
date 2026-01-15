package com.itu.compagnie_aerienne.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.itu.compagnie_aerienne.model.Client;
import com.itu.compagnie_aerienne.repository.ClientRepository;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
}
