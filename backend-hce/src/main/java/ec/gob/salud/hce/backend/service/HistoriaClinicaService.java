package ec.gob.salud.hce.backend.service;

import ec.gob.salud.hce.backend.dto.HistoriaClinicaRequestDTO;
import ec.gob.salud.hce.backend.dto.HistoriaClinicaResponseDTO;
import ec.gob.salud.hce.backend.entity.HistoriaClinica;
import java.util.List;
import java.util.Optional;

public interface HistoriaClinicaService {
    // Métodos para DTOs
    HistoriaClinicaResponseDTO crearHistoriaClinica(HistoriaClinicaRequestDTO dto);
    List<HistoriaClinicaResponseDTO> listarTodas();
    HistoriaClinicaResponseDTO obtenerPorId(Long id);
    
    // MÉTODO QUE FALTABA PARA EL CONTROLLER
    List<HistoriaClinicaResponseDTO> obtenerPorPaciente(Long idPaciente);

    // Métodos para lógica interna del ConsultaController
    HistoriaClinica obtenerOcrearHistoria(Integer idPaciente, String usuario);
    Optional<HistoriaClinica> buscarPorIdPaciente(Integer idPaciente);
}