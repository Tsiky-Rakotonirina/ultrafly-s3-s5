package com.itu.compagnie_aerienne.controller.reservation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReservationController {

    @GetMapping("/reservation")
    public String reservation() {
        return "The features for reservation are so close to launch ! "; 
    }
    
}
