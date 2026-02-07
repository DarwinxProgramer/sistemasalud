package ec.gob.salud.hce.backend.controller;

import ec.gob.salud.hce.backend.dto.SignoVitalDTO;
import ec.gob.salud.hce.backend.entity.SignoVital;
import ec.gob.salud.hce.backend.mapper.SignoVitalMapper;
import ec.gob.salud.hce.backend.repository.SignoVitalRepository; // Verifica que este sea el nombre exacto
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/signos-vitales")
public class SignoVitalController {

    @Autowired
    private SignoVitalRepository signoVitalRepository; // <--- Asegúrate que este nombre coincida abajo

    @GetMapping
    public List<SignoVitalDTO> getAll() {
        return SignoVitalMapper.toDtoList(signoVitalRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<SignoVitalDTO> create(@RequestBody SignoVitalDTO dto) {
        SignoVital entity = new SignoVital();

        // Mapeo de datos del DTO a la Entidad
        entity.setPeso(dto.getPeso());
        entity.setTallaLongitud(dto.getTallaLongitud());
        entity.setPerimetroCefalico(dto.getPerimetroCefalico());
        entity.setTemperatura(dto.getTemperatura());
        entity.setFrecuenciaCardiaca(dto.getFrecuenciaCardiaca());
        entity.setFrecuenciaRespiratoria(dto.getFrecuenciaRespiratoria());
        entity.setPresionArterialSistolica(dto.getPresionArterialSistolica());
        entity.setPresionArterialDiastolica(dto.getPresionArterialDiastolica());
        entity.setSaturacionOxigeno(dto.getSaturacionOxigeno());
        entity.setObservacion(dto.getObservacion());
        entity.setOrigin(dto.getOrigin());

        // Cálculo de IMC si hay datos
        if (dto.getPeso() != null && dto.getTallaLongitud() != null && dto.getTallaLongitud() > 0) {
            double tallaMetros = dto.getTallaLongitud() / 100;
            double imc = dto.getPeso() / (tallaMetros * tallaMetros);
            entity.setImc(Math.round(imc * 100.0) / 100.0);
        }

        // Guardar usando el nombre correcto del repositorio
        SignoVital guardado = signoVitalRepository.save(entity);

        return ResponseEntity.ok(SignoVitalMapper.toDto(guardado));
    }
}