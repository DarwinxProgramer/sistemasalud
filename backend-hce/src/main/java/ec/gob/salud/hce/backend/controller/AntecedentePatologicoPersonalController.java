package ec.gob.salud.hce.backend.controller;

import ec.gob.salud.hce.backend.dto.AntecedentePatologicoPersonalDTO;
import ec.gob.salud.hce.backend.entity.AntecedentePatologicoPersonal;
import ec.gob.salud.hce.backend.mapper.AntecedentePatologicoPersonalMapper;
import ec.gob.salud.hce.backend.repository.AntecedentePatologicoPersonalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/antecedentes-patologicos-personales")
public class AntecedentePatologicoPersonalController {

    @Autowired
    private AntecedentePatologicoPersonalRepository repository;

    @GetMapping
    public List<AntecedentePatologicoPersonalDTO> getAll() {
        return AntecedentePatologicoPersonalMapper.toDtoList(repository.findAll());
    }

    @PostMapping
    public ResponseEntity<AntecedentePatologicoPersonalDTO> create(@RequestBody AntecedentePatologicoPersonalDTO dto) {
        AntecedentePatologicoPersonal entity = new AntecedentePatologicoPersonal();
        
        // Mapeo explícito
        entity.setIdAntecedentePerinatal(dto.getIdAntecedentePerinatal());
        entity.setObservaciones(dto.getObservaciones());
        entity.setUsuario(dto.getUsuario());
        entity.setOrigin(dto.getOrigin());

        if (dto.getUuidOffline() != null) {
            entity.setUuidOffline(dto.getUuidOffline());
        }

        AntecedentePatologicoPersonal guardado = repository.save(entity);
        return ResponseEntity.ok(AntecedentePatologicoPersonalMapper.toDto(guardado));
    }
}