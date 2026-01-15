package com.itu.compagnie_aerienne.controller.avion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AvionController {

    @GetMapping("/avion")
    public String avion() {
        return "The features for avion are waiting to be implemented ! "; 
    }
    
}
