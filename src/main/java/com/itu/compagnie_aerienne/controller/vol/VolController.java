package com.itu.compagnie_aerienne.controller.vol;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class VolController {

    @GetMapping("/vol")
    public String vol() {
        return "The features for vol are coming soon ! "; 
    }
    
}
