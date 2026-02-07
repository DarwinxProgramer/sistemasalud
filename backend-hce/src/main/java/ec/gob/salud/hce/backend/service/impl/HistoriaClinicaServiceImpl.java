package ec.gob.salud.hce.backend.service.impl;

import ec.gob.salud.hce.backend.dto.HistoriaClinicaRequestDTO;
import ec.gob.salud.hce.backend.dto.HistoriaClinicaResponseDTO;
import ec.gob.salud.hce.backend.entity.HistoriaClinica;
import ec.gob.salud.hce.backend.mapper.HistoriaClinicaMapper;
import ec.gob.salud.hce.backend.repository.HistoriaClinicaRepository;
import ec.gob.salud.hce.backend.repository.PacienteRepository;
import ec.gob.salud.hce.backend.service.HistoriaClinicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoriaClinicaServiceImpl implements HistoriaClinicaService {

    private final HistoriaClinicaRepository historiaRepository;
    private final PacienteRepository pacienteRepository;

    @Override
    public HistoriaClinica obtenerOcrearHistoria(Integer idPaciente, String usuario) {
        return historiaRepository.findByPaciente_IdPaciente(idPaciente)
            .orElseGet(() -> {
                return pacienteRepository.findById(idPaciente).map(paciente -> {
                    HistoriaClinica nueva = new HistoriaClinica();
                    nueva.setPaciente(paciente);
                    nueva.setUsuario(usuario);
                    return historiaRepository.save(nueva);
                }).orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
            });
    }

    @Override
    public Optional<HistoriaClinica> buscarPorIdPaciente(Integer idPaciente) {
        return historiaRepository.findByPaciente_IdPaciente(idPaciente);
    }

    @Override
    public List<HistoriaClinicaResponseDTO> obtenerPorPaciente(Long idPaciente) {
        // Convertimos Long a Integer para el repositorio si es necesario
        return historiaRepository.findByPaciente_IdPaciente(idPaciente.intValue())
                .stream()
                .map(HistoriaClinicaMapper::toDto)
                .collect(Collectors.toList());
    }

    // Implementaciones básicas para que no de error de compilación
    @Override 
    public HistoriaClinicaResponseDTO crearHistoriaClinica(HistoriaClinicaRequestDTO dto) {
        return null; // Implementar lógica de guardado si es necesario
    }

    @Override 
    public List<HistoriaClinicaResponseDTO> listarTodas() {
        return historiaRepository.findAll().stream()
                .map(HistoriaClinicaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override 
    public HistoriaClinicaResponseDTO obtenerPorId(Long id) {
        return historiaRepository.findById(id)
                .map(HistoriaClinicaMapper::toDto)
                .orElse(null);
    }
}