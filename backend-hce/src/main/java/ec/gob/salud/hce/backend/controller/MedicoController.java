package ec.gob.salud.hce.backend.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medico")
public class MedicoController {

    @GetMapping("/test")
    public String medico() {
        return "Acceso MEDICO correcto";
    }
}
