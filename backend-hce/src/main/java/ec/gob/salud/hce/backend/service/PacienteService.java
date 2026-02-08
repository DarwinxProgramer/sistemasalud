package ec.gob.salud.hce.backend.service;

import ec.gob.salud.hce.backend.dto.PacienteRequestDTO;
import ec.gob.salud.hce.backend.dto.TutorDTO;
import ec.gob.salud.hce.backend.entity.*;
import ec.gob.salud.hce.backend.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    @Autowired
    private HistoriaClinicaRepository historiaClinicaRepository;

    // 🔥 CREAR PACIENTE + HISTORIA CLÍNICA AUTOMÁTICA
    @Transactional
    public Paciente crearPaciente(PacienteRequestDTO dto) {

        // 1️⃣ Crear Paciente
        Paciente paciente = new Paciente();
        paciente.setCedula(dto.getCedula());
        paciente.setPrimerNombre(dto.getPrimerNombre());
        paciente.setSegundoNombre(dto.getSegundoNombre());
        paciente.setApellidoPaterno(dto.getApellidoPaterno());
        paciente.setApellidoMaterno(dto.getApellidoMaterno());
        paciente.setFechaNacimiento(dto.getFechaNacimiento());
        paciente.setSexo(dto.getSexo());
        paciente.setTipoSangre(dto.getTipoSangre());
        paciente.setIdParroquia(dto.getIdParroquia());
        paciente.setIdPrqCanton(dto.getIdPrqCanton());
        paciente.setIdPrqCntProvincia(dto.getIdPrqCntProvincia());

        // ✅ UUID offline del paciente se mantiene
        paciente.setUuidOffline(dto.getUuidOffline());

        paciente = pacienteRepository.save(paciente);

        // 2️⃣ Crear Historia Clínica automáticamente
        HistoriaClinica historia = new HistoriaClinica();
        historia.setPaciente(paciente);

        // ✅ CORREGIDO
        historia.setFechaCreacion(LocalDate.now());

        historia.setSyncStatus("PENDING");

        historiaClinicaRepository.save(historia);

        // Vincular en memoria
        paciente.setHistoriaClinica(historia);

        // 3️⃣ Guardar Tutor si existe
        if (dto.getTutor() != null) {

            TutorDTO tutorDto = dto.getTutor();

            Tutor tutor = new Tutor();
            tutor.setPrimerNombre(tutorDto.getPrimerNombre());
            tutor.setSegundoNombre(tutorDto.getSegundoNombre());
            tutor.setPrimerApellido(tutorDto.getPrimerApellido());
            tutor.setSegundoApellido(tutorDto.getSegundoApellido());
            tutor.setTelefono(tutorDto.getTelefono());
            tutor.setNivelEducativo(tutorDto.getNivelEducativo());
            tutor.setDireccion(tutorDto.getDireccion());
            tutor.setIdParroquia(tutorDto.getIdParroquia());

            tutor = tutorRepository.save(tutor);

            PacienteTutor relacion = new PacienteTutor();
            relacion.setPaciente(paciente);
            relacion.setTutor(tutor);
            relacion.setParentesco(tutorDto.getParentesco());

            pacienteTutorRepository.save(relacion);
        }

        return paciente;
    }

    public List<Paciente> listarTodos() {
        return pacienteRepository.findAll();
    }

    public Optional<Paciente> obtenerPorId(Integer id) {
        return pacienteRepository.findById(id);
    }

    public List<Paciente> buscarPorCriterio(String criterio) {
        return pacienteRepository.buscarPorCriterio(criterio);
    }
}
