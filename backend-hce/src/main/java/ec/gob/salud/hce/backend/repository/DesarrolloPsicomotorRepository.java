package ec.gob.salud.hce.backend.repository;

import ec.gob.salud.hce.backend.entity.DesarrolloPsicomotor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DesarrolloPsicomotorRepository extends JpaRepository<DesarrolloPsicomotor, Integer> {
    
    // CORRECCIÓN:
    // 1. Nombre: findBy + HistoriaClinica (Entidad) + IdHistoriaClinica (Campo dentro de HistoriaClinica)
    // 2. Tipo: Long (Porque el ID de HistoriaClinica es Long)
    List<DesarrolloPsicomotor> findByHistoriaClinicaIdHistoriaClinica(Long idHistoriaClinica);
}