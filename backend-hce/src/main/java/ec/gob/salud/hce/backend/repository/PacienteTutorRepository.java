package ec.gob.salud.hce.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
// El siguiente import es CRUCIAL, debe apuntar a donde está tu clase modelo
import ec.gob.salud.hce.backend.entity.PacienteTutor; 

@Repository
public interface PacienteTutorRepository extends JpaRepository<PacienteTutor, Integer> {
}