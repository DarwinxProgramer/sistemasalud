package ec.gob.salud.hce.backend.service;

import ec.gob.salud.hce.backend.dto.SyncDownResponseDTO;
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

    @Autowired
    private com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public SyncDownResponseDTO obtenerDatosParaDescargaInicial() {
        System.out.println("DEBUG: Iniciando obtenerDatosParaDescargaInicial");
        try {
            SyncDownResponseDTO response = new SyncDownResponseDTO();

            // Cargar Pacientes con sus tutores
            System.out.println("DEBUG: Cargando Pacientes...");
            response.setPacientes(pacienteRepository.findAllWithTutores().stream()
                    .map(pacienteMapper::toDTO)
                    .collect(Collectors.toList()));
            System.out.println("DEBUG: Pacientes cargados: "
                    + (response.getPacientes() != null ? response.getPacientes().size() : 0));

            // Cargar Antecedentes
            System.out.println("DEBUG: Cargando Antecedentes...");
            response.setAntecedentesFamiliares(antecedenteFamiliarRepository.findAll().stream()
                    .map(anteMapper::toDTO)
                    .collect(Collectors.toList()));
            System.out.println("DEBUG: Antecedentes cargados: "
                    + (response.getAntecedentesFamiliares() != null ? response.getAntecedentesFamiliares().size() : 0));

            // Cargar Consultas completas (con planes y estudios)
            System.out.println("DEBUG: Cargando Consultas...");
            try {
                List<ec.gob.salud.hce.backend.entity.Consulta> consultas = consultaRepository.findAllWithDetails();
                System.out.println("DEBUG: Consultas encontradas en DB: " + (consultas != null ? consultas.size() : 0));

                if (consultas != null) {
                    List<ec.gob.salud.hce.backend.dto.ConsultaDTO> consultasDTO = consultas.stream()
                            .map(c -> {
                                try {
                                    return ec.gob.salud.hce.backend.mapper.ConsultaMapper.toDto(c, planMapper,
                                            estudioMapper);
                                } catch (Exception e) {
                                    System.err.println(
                                            "ERROR mapeando consulta ID " + c.getIdConsulta() + ": " + e.getMessage());
                                    e.printStackTrace();
                                    return null;
                                }
                            })
                            .filter(java.util.Objects::nonNull)
                            .collect(Collectors.toList());
                    response.setConsultas(consultasDTO);
                    System.out.println("DEBUG: Consultas mapeadas: " + consultasDTO.size());
                }
            } catch (Exception e) {
                System.err
                        .println("ERROR CRÍTICO recuperando consultas (Continuando con Pacientes): " + e.getMessage());
                e.printStackTrace();
                // Continuar sin consultas
                response.setConsultas(new java.util.ArrayList<>());
            }

            return response;
        } catch (Exception e) {
            System.err.println("CRITICAL ERROR in obtenerDatosParaDescargaInicial: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional
    public List<ec.gob.salud.hce.backend.dto.IdMappingDTO> procesarSubida(
            ec.gob.salud.hce.backend.dto.SyncUpRequestDTO request) {
        List<ec.gob.salud.hce.backend.dto.IdMappingDTO> mappings = new java.util.ArrayList<>();
        try {
            if ("paciente".equalsIgnoreCase(request.getEntity())) {
                if ("CREATE".equalsIgnoreCase(request.getType())) {
                    // Convertir el Map 'data' a PacienteRequestDTO
                    ec.gob.salud.hce.backend.dto.PacienteRequestDTO dto = objectMapper.convertValue(request.getData(),
                            ec.gob.salud.hce.backend.dto.PacienteRequestDTO.class);

                    // VERIFICAR SI YA EXISTE UN PACIENTE CON ESA CÉDULA
                    java.util.List<ec.gob.salud.hce.backend.entity.Paciente> existentes = pacienteRepository
                            .findByCedula(dto.getCedula());

                    if (existentes.isEmpty()) {
                        // Solo crear si no existe
                        ec.gob.salud.hce.backend.entity.Paciente nuevoPaciente = pacienteService.crearPaciente(dto);

                        // Agregar mapeo a la lista de respuesta
                        if (nuevoPaciente != null && nuevoPaciente.getUuidOffline() != null) {
                            mappings.add(new ec.gob.salud.hce.backend.dto.IdMappingDTO(
                                    nuevoPaciente.getUuidOffline(),
                                    nuevoPaciente.getIdPaciente(),
                                    "paciente"));
                            System.out.println("DEBUG: Mapeo creado UUID: " + nuevoPaciente.getUuidOffline()
                                    + " -> ID: " + nuevoPaciente.getIdPaciente());
                        }

                    } else {
                        // Paciente ya existe, devolver el mapeo del existente para que el frontend se
                        // actualice
                        ec.gob.salud.hce.backend.entity.Paciente existente = existentes.get(0);
                        if (dto.getUuidOffline() != null) {
                            mappings.add(new ec.gob.salud.hce.backend.dto.IdMappingDTO(
                                    dto.getUuidOffline(),
                                    existente.getIdPaciente(),
                                    "paciente"));
                        }
                        System.out.println(
                                "Paciente con cédula " + dto.getCedula() + " ya existe. Devolviendo ID existente: "
                                        + existente.getIdPaciente());
                    }
                }
                // TODO: Implementar UPDATE y DELETE si es necesario
            }
            return mappings;
        } catch (Exception e) {
            System.err.println("❌ ERROR en procesarSubida:");
            System.err.println("Entity: " + request.getEntity());
            System.err.println("Type: " + request.getType());
            System.err.println("Data: " + request.getData());
            System.err.println("Exception: " + e.getClass().getName());
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al procesar sincronización: " + e.getMessage(), e);
        }
    }
}