package ec.gob.salud.hce.backend.service;

import ec.gob.salud.hce.backend.dto.ConsultaDTO;
import ec.gob.salud.hce.backend.entity.*;
import ec.gob.salud.hce.backend.mapper.ConsultaMapper;
import ec.gob.salud.hce.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional; // Asegúrate de tener este import
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final HistoriaClinicaRepository historiaRepository;
    private final ec.gob.salud.hce.backend.mapper.PlanTerapeuticoMapper planMapper;
    private final ec.gob.salud.hce.backend.mapper.EstudioLaboratorioMapper estudioMapper;

    /**
     * Lógica de Sincronización Offline
     * Busca por UUID: si existe, actualiza; si no, crea.
     */
    @Transactional
    public ConsultaDTO guardarSincronizado(ConsultaDTO dto) {
        // 1. Intentar buscar por el UUID que viene del frontend (Offline)
        Optional<Consulta> consultaExistente = consultaRepository.findByUuidOffline(dto.getUuidOffline());

        if (consultaExistente.isPresent()) {
            // Lógica de ACTUALIZACIÓN
            Consulta consulta = consultaExistente.get();

            // Actualizamos solo los campos necesarios de la consulta base
            consulta.setMotivoConsulta(dto.getMotivo());
            consulta.setEnfermedadActual(dto.getEnfermedadActual());
            consulta.setDiagnosticoPrincipal(dto.getDiagnosticoTexto());
            // Nota: Aquí podrías actualizar planes y estudios si tu lógica lo requiere

            Consulta guardada = consultaRepository.save(consulta);
            return ConsultaMapper.toDto(guardada, planMapper, estudioMapper);
        } else {
            // Lógica de CREACIÓN (Reutilizamos tu método existente)
            return guardarConsultaCompleta(dto);
        }
    }

    @Transactional
    public ConsultaDTO guardarConsultaCompleta(ConsultaDTO dto) {
        Paciente paciente = pacienteRepository.findById(dto.getIdPaciente())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        HistoriaClinica historia = historiaRepository.findByPaciente_IdPaciente(paciente.getIdPaciente())
                .orElseGet(() -> {
                    HistoriaClinica nueva = new HistoriaClinica();
                    nueva.setPaciente(paciente);
                    nueva.setUsuario(dto.getUsuario());
                    return historiaRepository.save(nueva);
                });

        Consulta consulta = ConsultaMapper.toEntity(dto, paciente, planMapper, estudioMapper);
        consulta.setIdHistoriaClinica(historia.getIdHistoriaClinica().intValue());

        // Seteamos el UUID offline para que en la próxima sincronización lo encuentre
        if (dto.getUuidOffline() != null) {
            consulta.setUuidOffline(dto.getUuidOffline());
        }

        if (dto.getUsuario() != null)
            consulta.setUsuarioMedico(dto.getUsuario());

        Consulta consultaGuardada = consultaRepository.save(consulta);

        return ConsultaMapper.toDto(consultaGuardada, planMapper, estudioMapper);
    }

    // ... (Tus métodos listarPorPaciente y listarTodas se mantienen igual)

    @Transactional(readOnly = true)
    public List<ConsultaDTO> listarPorPaciente(Integer idPaciente) {
        return consultaRepository.findByIdPacienteWithDetails(idPaciente)
                .stream()
                .map(c -> ConsultaMapper.toDto(c, planMapper, estudioMapper))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ConsultaDTO> listarTodas() {
        return consultaRepository.findAll().stream().map(c -> {
            ConsultaDTO dto = ConsultaMapper.toDto(c);
            dto.setUsuario(c.getUsuarioMedico());
            return dto;
        }).collect(Collectors.toList());
    }
}