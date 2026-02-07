package ec.gob.salud.hce.backend.controller;

import ec.gob.salud.hce.backend.dto.AntecedenteInmunizacionDTO;
import ec.gob.salud.hce.backend.entity.AntecedenteInmunizacion;
import ec.gob.salud.hce.backend.mapper.AntecedenteInmunizacionMapper;
import ec.gob.salud.hce.backend.repository.AntecedenteInmunizacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/antecedentes-inmunizacion")
public class AntecedenteInmunizacionController {

    @Autowired
    private AntecedenteInmunizacionRepository repository;

    // Obtener por Paciente (Esto es lo que fallaba antes si la entidad no tenía la relación)
    @GetMapping("/paciente/{idPaciente}")
    public List<AntecedenteInmunizacionDTO> getByPaciente(@PathVariable Integer idPaciente) {
        return AntecedenteInmunizacionMapper.toDtoList(
            repository.findByHistoriaClinica_Paciente_IdPaciente(idPaciente)
        );
    }

    @GetMapping
    public List<AntecedenteInmunizacionDTO> getAll() {
        return AntecedenteInmunizacionMapper.toDtoList(repository.findAll());
    }

    @PostMapping
    public ResponseEntity<AntecedenteInmunizacionDTO> create(@RequestBody AntecedenteInmunizacionDTO dto) {
        // Usamos el Mapper para convertir correctamente las relaciones
        AntecedenteInmunizacion entity = AntecedenteInmunizacionMapper.toEntity(dto);
        AntecedenteInmunizacion guardado = repository.save(entity);
        return ResponseEntity.ok(AntecedenteInmunizacionMapper.toDto(guardado));
    }
}