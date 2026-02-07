package ec.gob.salud.hce.backend.repository;

import ec.gob.salud.hce.backend.entity.AntecedenteInmunizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AntecedenteInmunizacionRepository extends JpaRepository<AntecedenteInmunizacion, Integer> {
    
    // ESTA ES LA CLAVE:
    // Busca las inmunizaciones navegando a través de la Historia Clínica hasta el Paciente
    // Nota: Esto requiere que tu entidad AntecedenteInmunizacion tenga la relación @ManyToOne con HistoriaClinica
    List<AntecedenteInmunizacion> findByHistoriaClinica_Paciente_IdPaciente(Integer idPaciente);
}