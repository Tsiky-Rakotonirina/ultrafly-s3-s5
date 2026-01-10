package com.itu.compagnie_aerienne.controller.dashboard;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "The features for dashboard are not finished yet ! "; 
    }
    
}
