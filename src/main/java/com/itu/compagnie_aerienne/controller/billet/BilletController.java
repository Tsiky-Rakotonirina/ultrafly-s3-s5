package com.itu.compagnie_aerienne.controller.billet;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class BilletController {

    @GetMapping("/billet")
    public String billet() {
        return "The features for billet are so close to launch ! "; 
    }
    
}
