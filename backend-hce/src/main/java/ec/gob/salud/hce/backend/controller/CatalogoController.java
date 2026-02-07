package ec.gob.salud.hce.backend.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
// He eliminado el import de CrossOrigin porque no se usa
import ec.gob.salud.hce.backend.entity.GrupoEtnico;
import ec.gob.salud.hce.backend.repository.GrupoEtnicoRepository;

@RestController
@RequestMapping("/api/catalogos")
// ¡OJO! Aquí NO pongas @CrossOrigin. Deja que WebConfig haga su trabajo.
public class CatalogoController {

    @Autowired
    private GrupoEtnicoRepository grupoEtnicoRepository;

    @GetMapping("/etnias")
    public List<GrupoEtnico> listarEtnias() {
        return grupoEtnicoRepository.findAll();
    }
}