package com.itu.compagnie_aerienne.controller.enregistrement;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class EnregistrementController {
    @GetMapping("/enregistrement")
    public String enregistrement() {
        return "The features for enregistrement are under development ! "; 
    }
}
