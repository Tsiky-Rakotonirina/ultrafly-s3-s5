package com.itu.compagnie_aerienne.controller.enregistrement;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EnregistrementController {
    @GetMapping("/enregistrement")
    public String enregistrement() {
        return "The features for enregistrement are under development ! "; 
    }
}
