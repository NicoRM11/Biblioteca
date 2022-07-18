package com.egg.biblioteca.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/admin") //se puede hacer una preautorizacion a nivel clase, pero lo vamos a hace de otra manera
public class AdminControlador {
    
    @GetMapping("/dashboard")
    public String panelAdministrativo(){    
        return "panel.html";
    }
    
    
}
