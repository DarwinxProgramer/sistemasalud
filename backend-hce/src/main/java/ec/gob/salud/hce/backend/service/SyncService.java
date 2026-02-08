package ec.gob.salud.hce.backend.service;

import ec.gob.salud.hce.backend.dto.SyncDownResponseDTO;
import ec.gob.salud.hce.backend.dto.ConsultaDTO; // Importante
import ec.gob.salud.hce.backend.dto.IdMappingDTO; // Importante
import ec.gob.salud.hce.backend.mapper.PacienteMapper;
import ec.gob.salud.hce.backend.mapper.AntecedenteFamiliarMapper;
import ec.gob.salud.hce.backend.repository.PacienteRepository;
import ec.gob.salud.hce.backend.repository.AntecedenteFamiliarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import java.util.List;

@Service
public class SyncService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private AntecedenteFamiliarRepository antecedenteFamiliarRepository;

    @Autowired
    private PacienteMapper pacienteMapper;

    @Autowired
    private AntecedenteFamiliarMapper anteMapper;

    @Autowired
    private ec.gob.salud.hce.backend.repository.ConsultaRepository consultaRepository;

    @Autowired
    private ec.gob.salud.hce.backend.mapper.PlanTerapeuticoMapper planMapper;

    @Autowired
    private ec.gob.salud.hce.backend.mapper.EstudioLaboratorioMapper estudioMapper;

    @Autowired
    private PacienteService pacienteService;

    // --- NUEVA INYECCIÓN PARA CONSULTAS ---
    @Autowired
    private ConsultaService consultaService;
    // --------------------------------------

    @Autowired
    private com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public SyncDownResponseDTO obtenerDatosParaDescargaInicial() {
        // ... (Tu código de descarga se mantiene igual)
        System.out.println("DEBUG: Iniciando obtenerDatosParaDescargaInicial");
        try {
            SyncDownResponseDTO response = new SyncDownResponseDTO();

            // Cargar Pacientes con sus tutores
            System.out.println("DEBUG: Cargando Pacientes...");
            response.setPacientes(pacienteRepository.findAllWithTutores().stream()
                    .map(pacienteMapper::toDTO)
                    .collect(Collectors.toList()));

            // Cargar Antecedentes
            response.setAntecedentesFamiliares(antecedenteFamiliarRepository.findAll().stream()
                    .map(anteMapper::toDTO)
                    .collect(Collectors.toList()));

            // Cargar Consultas completas
            try {
                List<ec.gob.salud.hce.backend.entity.Consulta> consultas = consultaRepository.findAllWithDetails();
                if (consultas != null) {
                    List<ec.gob.salud.hce.backend.dto.ConsultaDTO> consultasDTO = consultas.stream()
                            .map(c -> ec.gob.salud.hce.backend.mapper.ConsultaMapper.toDto(c, planMapper,
                                    estudioMapper))
                            .filter(java.util.Objects::nonNull)
                            .collect(Collectors.toList());
                    response.setConsultas(consultasDTO);
                }
            } catch (Exception e) {
                response.setConsultas(new java.util.ArrayList<>());
            }

            return response;
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public List<ec.gob.salud.hce.backend.dto.IdMappingDTO> procesarSubida(
            ec.gob.salud.hce.backend.dto.SyncUpRequestDTO request) {
        List<ec.gob.salud.hce.backend.dto.IdMappingDTO> mappings = new java.util.ArrayList<>();
        try {
            // --- LÓGICA EXISTENTE PARA PACIENTE ---
            if ("paciente".equalsIgnoreCase(request.getEntity())) {
                if ("CREATE".equalsIgnoreCase(request.getType())) {
                    ec.gob.salud.hce.backend.dto.PacienteRequestDTO dto = objectMapper.convertValue(request.getData(),
                            ec.gob.salud.hce.backend.dto.PacienteRequestDTO.class);

                    java.util.List<ec.gob.salud.hce.backend.entity.Paciente> existentes = pacienteRepository
                            .findByCedula(dto.getCedula());

                    if (existentes.isEmpty()) {
                        ec.gob.salud.hce.backend.entity.Paciente nuevoPaciente = pacienteService.crearPaciente(dto);
                        if (nuevoPaciente != null && nuevoPaciente.getUuidOffline() != null) {
                            mappings.add(new ec.gob.salud.hce.backend.dto.IdMappingDTO(
                                    nuevoPaciente.getUuidOffline(),
                                    nuevoPaciente.getIdPaciente(),
                                    "paciente"));
                        }
                    } else {
                        ec.gob.salud.hce.backend.entity.Paciente existente = existentes.get(0);
                        if (dto.getUuidOffline() != null) {
                            mappings.add(new ec.gob.salud.hce.backend.dto.IdMappingDTO(
                                    dto.getUuidOffline(),
                                    existente.getIdPaciente(),
                                    "paciente"));
                        }
                    }
                }
            }

            // --- NUEVA LÓGICA PARA CONSULTA (AGREGADO) ---
            else if ("consulta".equalsIgnoreCase(request.getEntity())) {
                // Convertir el Map 'data' a ConsultaDTO
                ConsultaDTO dto = objectMapper.convertValue(request.getData(), ConsultaDTO.class);

                // Usar el método que creamos en ConsultaService para manejar el UUID
                ConsultaDTO guardada = consultaService.guardarSincronizado(dto);

                // Registrar el mapeo (UUID Offline -> ID generado por DB)
                if (guardada != null && guardada.getUuidOffline() != null) {
                    mappings.add(new IdMappingDTO(
                            guardada.getUuidOffline(),
                            guardada.getIdConsulta().intValue(), // Convertir Long a Integer si es necesario
                            "consulta"));
                    System.out.println("DEBUG SYNC: Consulta sincronizada UUID: " + guardada.getUuidOffline()
                            + " -> ID DB: " + guardada.getIdConsulta());
                }
            }
            // ---------------------------------------------

            return mappings;
        } catch (Exception e) {
            System.err.println("❌ ERROR en procesarSubida:");
            e.printStackTrace();
            throw new RuntimeException("Error al procesar sincronización: " + e.getMessage(), e);
        }
    }
}