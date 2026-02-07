package ec.gob.salud.hce.backend.service;

import ec.gob.salud.hce.backend.dto.PacienteRequestDTO;
import ec.gob.salud.hce.backend.dto.TutorDTO;
import ec.gob.salud.hce.backend.entity.Paciente;
import ec.gob.salud.hce.backend.entity.PacienteTutor;
import ec.gob.salud.hce.backend.entity.Tutor;
import ec.gob.salud.hce.backend.repository.PacienteRepository;
import ec.gob.salud.hce.backend.repository.PacienteTutorRepository;
import ec.gob.salud.hce.backend.repository.TutorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private PacienteTutorRepository pacienteTutorRepository;

    // --- 1. CREAR PACIENTE (Renombrado para coincidir con el Controller) ---
    @Transactional
    public Paciente crearPaciente(PacienteRequestDTO dto) {

        // A. GUARDAR DATOS DEL PACIENTE
        Paciente paciente = new Paciente();
        paciente.setCedula(dto.getCedula());
        paciente.setPrimerNombre(dto.getPrimerNombre());
        paciente.setSegundoNombre(dto.getSegundoNombre());
        paciente.setApellidoPaterno(dto.getApellidoPaterno());
        paciente.setApellidoMaterno(dto.getApellidoMaterno());
        paciente.setFechaNacimiento(dto.getFechaNacimiento());
        paciente.setSexo(dto.getSexo());
        paciente.setTipoSangre(dto.getTipoSangre());

        // CORRECCIÓN DE TIPOS: Pasamos Integer directamente (sin .longValue())
        paciente.setIdParroquia(dto.getIdParroquia());
        paciente.setIdPrqCanton(dto.getIdPrqCanton());
        paciente.setIdPrqCntProvincia(dto.getIdPrqCntProvincia());
        paciente.setUuidOffline(dto.getUuidOffline()); // Guardar UUID Offline

        // Guardar Paciente
        paciente = pacienteRepository.save(paciente);

        // B. GUARDAR TUTOR (Si el DTO lo trae)
        if (dto.getTutor() != null) {
            TutorDTO tutorDto = dto.getTutor();

            Tutor tutor = new Tutor();
            // Mapeamos los campos del Tutor
            tutor.setPrimerNombre(tutorDto.getPrimerNombre());
            tutor.setSegundoNombre(tutorDto.getSegundoNombre());
            tutor.setPrimerApellido(tutorDto.getPrimerApellido());
            tutor.setSegundoApellido(tutorDto.getSegundoApellido());
            tutor.setTelefono(tutorDto.getTelefono());
            tutor.setNivelEducativo(tutorDto.getNivelEducativo());
            tutor.setDireccion(tutorDto.getDireccion());

            // Ubicación Tutor (Directo Integer)
            tutor.setIdParroquia(tutorDto.getIdParroquia());

            // Guardar Tutor
            tutor = tutorRepository.save(tutor);

            // C. GUARDAR RELACIÓN (Tabla Intermedia)
            PacienteTutor relacion = new PacienteTutor();
            relacion.setPaciente(paciente);
            relacion.setTutor(tutor);
            relacion.setParentesco(tutorDto.getParentesco());

            pacienteTutorRepository.save(relacion);
        }

        return paciente;
    }

    // --- 2. LISTAR TODOS (Faltaba este método) ---
    public List<Paciente> listarTodos() {
        return pacienteRepository.findAll();
    }

    // --- 3. OBTENER POR ID (Faltaba este método) ---
    public Optional<Paciente> obtenerPorId(Integer id) {
        return pacienteRepository.findById(id);
    }

    public List<Paciente> buscarPorCriterio(String criterio) {
        return pacienteRepository.buscarPorCriterio(criterio);
    }
}