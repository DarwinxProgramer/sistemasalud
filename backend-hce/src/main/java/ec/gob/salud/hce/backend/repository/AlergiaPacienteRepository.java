package ec.gob.salud.hce.backend.repository;

import ec.gob.salud.hce.backend.entity.AlergiaPaciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlergiaPacienteRepository extends JpaRepository<AlergiaPaciente, Integer> {
    
    // Gracias a @ManyToOne Paciente, esto funciona perfectamente
    List<AlergiaPaciente> findByPaciente_IdPaciente(Integer idPaciente);
}