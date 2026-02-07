package ec.gob.salud.hce.backend.repository;

import ec.gob.salud.hce.backend.entity.HistoriaClinica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface HistoriaClinicaRepository extends JpaRepository<HistoriaClinica, Long> {

    // --- ESTA ES LA LÍNEA QUE FALTA ---
    // Spring usará la relación "paciente" y buscará por su "idPaciente"
    Optional<HistoriaClinica> findByPaciente_IdPaciente(Integer idPaciente);
    
}