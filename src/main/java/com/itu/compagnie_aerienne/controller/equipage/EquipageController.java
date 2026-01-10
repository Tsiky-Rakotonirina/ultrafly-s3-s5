package com.itu.compagnie_aerienne.controller.equipage;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class EquipageController {
    @GetMapping("/equipage")
    public String equipage() {
        return "The features for equipage are under development ! "; 
    }
}
