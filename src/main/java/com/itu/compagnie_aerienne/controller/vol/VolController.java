package com.itu.compagnie_aerienne.controller.vol;

import org.springframework.web.bind.annotation.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VolController {

    @GetMapping("/vol")
    public String vol() {
        return "vol-liste"; 
    }
    
}
