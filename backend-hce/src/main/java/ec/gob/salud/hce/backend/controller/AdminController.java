package ec.gob.salud.hce.backend.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/test")
    public String admin() {
        return "Acceso ADMIN correcto";
    }
}
