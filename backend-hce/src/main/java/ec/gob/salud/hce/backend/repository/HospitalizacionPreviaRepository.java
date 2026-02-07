package ec.gob.salud.hce.backend.repository;

import ec.gob.salud.hce.backend.entity.HospitalizacionPrevia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HospitalizacionPreviaRepository extends JpaRepository<HospitalizacionPrevia, Integer> {
    // Buscar por Paciente
    List<HospitalizacionPrevia> findByPaciente_IdPaciente(Integer idPaciente);
}