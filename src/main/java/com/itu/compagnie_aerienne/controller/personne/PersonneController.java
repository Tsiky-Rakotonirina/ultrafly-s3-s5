package com.itu.compagnie_aerienne.controller.personne;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PersonneController {

    @GetMapping("/personne")
    public String personne() {
        return "The features for personne are just around the corner ! "; 
    }
    
}
