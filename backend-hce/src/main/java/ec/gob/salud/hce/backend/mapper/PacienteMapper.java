package ec.gob.salud.hce.backend.mapper;

import ec.gob.salud.hce.backend.dto.PacienteRequestDTO;
import ec.gob.salud.hce.backend.dto.PacienteResponseDTO;
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

        // --- CORRECCIÓN: ASIGNACIÓN DIRECTA DE IDs ---
        // Antes: dto.setIdGrupoEtnico(entity.getGrupoEtnico().getId());
        // Ahora: entity.getIdGrupoEtnico() ya devuelve el Integer
        dto.setIdGrupoEtnico(entity.getIdGrupoEtnico());

        // Antes: dto.setIdParroquia(entity.getParroquia().getId());
        dto.setIdParroquia(entity.getIdParroquia());

        dto.setIdPrqCanton(entity.getIdPrqCanton());
        dto.setIdPrqCntProvincia(entity.getIdPrqCntProvincia());

        // Auditoría
        dto.setUsuario(entity.getUsuario());
        dto.setUuidOffline(entity.getUuidOffline());
        dto.setSyncStatus(entity.getSyncStatus());
        dto.setLastModified(entity.getLastModified());
        dto.setOrigin(entity.getOrigin());
        dto.setIdPersonal(entity.getIdPersonal());

        // Mapear tutor si existe relación
        if (entity.getPacientesTutores() != null && !entity.getPacientesTutores().isEmpty()) {
            // Tomar el primer tutor (asumiendo un paciente tiene un tutor principal)
            PacienteTutor pt = entity.getPacientesTutores().get(0);
            if (pt.getTutor() != null) {
                // Necesitamos TutorMapper inyectado
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

        // Si es actualización, setearíamos el ID aquí
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

        // --- CORRECCIÓN: ASIGNACIÓN DIRECTA DE IDs ---
        // Ya no necesitamos crear objetos 'new GrupoEtnico()' o 'new Parroquia()'
        // Solo pasamos el número entero.

        entity.setIdGrupoEtnico(dto.getIdGrupoEtnico());
        entity.setIdParroquia(dto.getIdParroquia());

        entity.setIdPrqCanton(dto.getIdPrqCanton());
        entity.setIdPrqCntProvincia(dto.getIdPrqCntProvincia());

        // Auditoría básica desde el DTO si viene
        entity.setUsuario(dto.getUsuario());
        entity.setUuidOffline(dto.getUuidOffline());
        entity.setOrigin(dto.getOrigin());
        entity.setIdPersonal(dto.getIdPersonal());

        return entity;
    }
}