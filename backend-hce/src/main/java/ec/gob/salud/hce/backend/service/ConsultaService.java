package ec.gob.salud.hce.backend.service;

import ec.gob.salud.hce.backend.dto.ConsultaDTO;
import ec.gob.salud.hce.backend.entity.*;
import ec.gob.salud.hce.backend.mapper.ConsultaMapper;
import ec.gob.salud.hce.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final HistoriaClinicaRepository historiaRepository;
    private final ec.gob.salud.hce.backend.mapper.PlanTerapeuticoMapper planMapper;
    private final ec.gob.salud.hce.backend.mapper.EstudioLaboratorioMapper estudioMapper;

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

        // Crear consulta con mapper (ahora incluye planes y estudios con cascade)
        Consulta consulta = ConsultaMapper.toEntity(dto, paciente, planMapper, estudioMapper);
        consulta.setIdHistoriaClinica(historia.getIdHistoriaClinica().intValue());
        if (dto.getUsuario() != null)
            consulta.setUsuarioMedico(dto.getUsuario());

        // UN SOLO SAVE - cascade automático guarda planes y estudios
        Consulta consultaGuardada = consultaRepository.save(consulta);

        return ConsultaMapper.toDto(consultaGuardada, planMapper, estudioMapper);
    }

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