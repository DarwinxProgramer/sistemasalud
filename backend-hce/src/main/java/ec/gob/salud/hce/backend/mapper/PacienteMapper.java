package ec.gob.salud.hce.backend.mapper;

import ec.gob.salud.hce.backend.dto.PacienteRequestDTO;
import ec.gob.salud.hce.backend.dto.PacienteResponseDTO;
import ec.gob.salud.hce.backend.entity.HistoriaClinica;
import ec.gob.salud.hce.backend.entity.Paciente;
import ec.gob.salud.hce.backend.entity.PacienteTutor;
import org.springframework.stereotype.Component;

@Component
public class PacienteMapper {

    // --- 1. CONVERTIR DE ENTIDAD (BD) A DTO (RESPONSE) ---
    public PacienteResponseDTO toDTO(Paciente entity) {
        if (entity == null) {
            return null;
        }

        PacienteResponseDTO dto = new PacienteResponseDTO();

        dto.setIdPaciente(entity.getIdPaciente());
        dto.setCedula(entity.getCedula());
        dto.setPrimerNombre(entity.getPrimerNombre());
        dto.setSegundoNombre(entity.getSegundoNombre());
        dto.setApellidoPaterno(entity.getApellidoPaterno());
        dto.setApellidoMaterno(entity.getApellidoMaterno());
        dto.setFechaNacimiento(entity.getFechaNacimiento());
        dto.setSexo(entity.getSexo());
        dto.setTipoSangre(entity.getTipoSangre());

        dto.setIdParroquia(entity.getIdParroquia());
        dto.setIdPrqCanton(entity.getIdPrqCanton());
        dto.setIdPrqCntProvincia(entity.getIdPrqCntProvincia());

        // ===============================
        // 🔥 DATOS QUE AHORA ESTÁN EN HISTORIA CLÍNICA
        // ===============================

        HistoriaClinica historia = entity.getHistoriaClinica();

        if (historia != null) {
            dto.setIdGrupoEtnico(historia.getIdGrupoEtnico());
            dto.setUsuario(historia.getUsuario());
            dto.setUuidOffline(historia.getUuidOffline());
            dto.setSyncStatus(historia.getSyncStatus());
            dto.setLastModified(historia.getLastModified());
            dto.setOrigin(historia.getOrigin());
            dto.setIdPersonal(historia.getIdPersonal());
        }

        // ===============================
        // MAPEAR TUTOR
        // ===============================

        if (entity.getPacientesTutores() != null && !entity.getPacientesTutores().isEmpty()) {

            PacienteTutor pt = entity.getPacientesTutores().get(0);

            if (pt.getTutor() != null) {
                ec.gob.salud.hce.backend.mapper.TutorMapper tutorMapper = new ec.gob.salud.hce.backend.mapper.TutorMapper();

                ec.gob.salud.hce.backend.dto.TutorDTO tutorDTO = tutorMapper.toDTO(pt.getTutor());

                tutorDTO.setParentesco(pt.getParentesco());
                dto.setTutor(tutorDTO);
            }
        }

        return dto;
    }

    // --- 2. CONVERTIR DE DTO (REQUEST) A ENTIDAD (BD) ---
    public Paciente toEntity(PacienteRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Paciente entity = new Paciente();

        if (dto.getIdPaciente() != null) {
            entity.setIdPaciente(dto.getIdPaciente());
        }

        entity.setCedula(dto.getCedula());
        entity.setPrimerNombre(dto.getPrimerNombre());
        entity.setSegundoNombre(dto.getSegundoNombre());
        entity.setApellidoPaterno(dto.getApellidoPaterno());
        entity.setApellidoMaterno(dto.getApellidoMaterno());
        entity.setFechaNacimiento(dto.getFechaNacimiento());
        entity.setSexo(dto.getSexo());
        entity.setTipoSangre(dto.getTipoSangre());

        entity.setIdParroquia(dto.getIdParroquia());
        entity.setIdPrqCanton(dto.getIdPrqCanton());
        entity.setIdPrqCntProvincia(dto.getIdPrqCntProvincia());

        // 🔥 IMPORTANTE:
        // Los datos clínicos NO se setean aquí.
        // Se deben setear en el Service creando o actualizando HistoriaClinica.

        return entity;
    }
}
