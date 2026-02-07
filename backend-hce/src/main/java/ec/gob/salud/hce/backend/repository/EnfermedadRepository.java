package ec.gob.salud.hce.backend.repository;

import ec.gob.salud.hce.backend.entity.Enfermedad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnfermedadRepository extends JpaRepository<Enfermedad, Integer> {
}