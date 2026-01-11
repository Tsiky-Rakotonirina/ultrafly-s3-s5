package com.itu.compagnie_aerienne.controller.avion;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class AvionController {

    @GetMapping("/avion")
    public String avion() {
        return "The features for avion are waiting to be implemented ! "; 
    }
    
}
