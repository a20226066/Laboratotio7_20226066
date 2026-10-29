package pe.edu.pucp.gtics.doctormanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // La clase responde a peticiones HTTP y devuelve vistas HTML.
public class HomeController {
    @GetMapping("/") // La ruta principal muestra index.html en templates.
    public String inicio() {
        return "index";
    }
}
