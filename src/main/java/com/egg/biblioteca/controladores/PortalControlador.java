package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Usuario;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class PortalControlador { //localhost:8080/

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/")  //localhost:8080/
    public String index() {
        return "index.html";

    }

    @GetMapping("/registrar")
    public String registrar() {
        return "registro.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, @RequestParam String email, @RequestParam String password, @RequestParam String password2,
            ModelMap modelo) {

        try {
            usuarioServicio.registrar(nombre, email, password, password2);

            modelo.put("exito", "Usted se ah registrado exitosamente");

            return "index.html";

        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            
            return "registro.html";
        }

    }

    
    
    @GetMapping("/login") //en este metodo se avisa por parametro que puede venir o no, algun error
    public String login(@RequestParam(required = false) String error  , ModelMap modelo) {
        
        if (error != null) {
            modelo.put("error", "Usuario o clave incorrectos");
        }
        
        return "login.html";
    }
    
    
    @PreAuthorize("hasAnyRole('ROLE_USER' , 'ROLE_ADMIN')") //configuracion que para acceder  esta vista, se debe estar registrado de manera previa
    @GetMapping("/inicio")
    public String inicio(HttpSession session){
        
       Usuario logueado = (Usuario) session.getAttribute("usuariosession"); //
        
        if (logueado.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }
 
        
       return "inicio.html";
    }

}
