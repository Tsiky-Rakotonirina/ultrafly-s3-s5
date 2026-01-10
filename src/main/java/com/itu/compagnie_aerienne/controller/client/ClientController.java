package com.itu.compagnie_aerienne.controller.client;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class ClientController {

    @GetMapping("/client")
    public String client() {
        return "The features for client are just around the corner ! "; 
    }
    
}
