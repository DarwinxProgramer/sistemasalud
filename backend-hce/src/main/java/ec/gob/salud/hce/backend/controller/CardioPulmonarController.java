package ec.gob.salud.hce.backend.controller;

import ec.gob.salud.hce.backend.dto.CardioPulmonarDTO;
import ec.gob.salud.hce.backend.entity.CardioPulmonar;
import ec.gob.salud.hce.backend.mapper.CardioPulmonarMapper;
import ec.gob.salud.hce.backend.repository.CardioPulmonarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cardio-pulmonares")
public class CardioPulmonarController {

    @Autowired
    private CardioPulmonarRepository repository;

    @GetMapping
    public List<CardioPulmonarDTO> getAll() {
        return CardioPulmonarMapper.toDtoList(repository.findAll());
    }

    @PostMapping
    public ResponseEntity<CardioPulmonarDTO> create(@RequestBody CardioPulmonarDTO dto) {
        CardioPulmonar entity = CardioPulmonarMapper.toEntity(dto);
        CardioPulmonar guardado = repository.save(entity);
        return ResponseEntity.ok(CardioPulmonarMapper.toDto(guardado));
    }
}