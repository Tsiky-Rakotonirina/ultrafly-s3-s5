package com.itu.compagnie_aerienne.controller.aeroport;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class AeroportController {

    @GetMapping("/aeroport")
    public String aeroport() {
        return "The features for aeroport are waiting to be implemented ! "; 
    }
    
}
