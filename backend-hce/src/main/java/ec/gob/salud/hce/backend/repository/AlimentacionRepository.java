package ec.gob.salud.hce.backend.repository;

import ec.gob.salud.hce.backend.entity.Alimentacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlimentacionRepository extends JpaRepository<Alimentacion, Integer> {
    // Puedes agregar búsqueda por desarrollo psicomotor si lo necesitas
    // List<Alimentacion> findByDesarrolloPsicomotor_IdDesarrolloPsicomotor(Integer id);
}