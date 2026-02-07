package ec.gob.salud.hce.backend.repository; // <--- ESTO FALTABA

import ec.gob.salud.hce.backend.entity.CirugiaPrevia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List; // <--- Importante para que reconozca List
@Repository
public interface CirugiaPreviaRepository extends JpaRepository<CirugiaPrevia, Integer> {
    
    // Buscar por ID de Paciente (Usando la nueva relación)
    List<CirugiaPrevia> findByPaciente_IdPaciente(Integer idPaciente);
    
    // Mantienes la búsqueda anterior si la necesitas
    List<CirugiaPrevia> findByIdAntecedentePatologicoPersonal(Integer idAntecedente);
}