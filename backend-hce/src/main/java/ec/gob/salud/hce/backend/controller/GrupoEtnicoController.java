package ec.gob.salud.hce.backend.controller;

import ec.gob.salud.hce.backend.dto.GrupoEtnicoDTO;
import ec.gob.salud.hce.backend.mapper.GrupoEtnicoMapper;
import ec.gob.salud.hce.backend.repository.GrupoEtnicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
// AQUI ESTA LA CLAVE: Cambiamos la URL para que no diga "catalogos"
@RequestMapping("/api/grupos-etnicos") 
@CrossOrigin(origins = "*")
public class GrupoEtnicoController {

    @Autowired
    private GrupoEtnicoRepository repository;

    @Autowired
    private GrupoEtnicoMapper mapper;

    @GetMapping
    public List<GrupoEtnicoDTO> getAll() {
        // Esto va a buscar a la tabla "grupos_etnicos" gracias a tu ENTIDAD, no al nombre del controller.
        return mapper.toDTOList(repository.findAll());
    }
}