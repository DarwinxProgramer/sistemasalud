package ec.gob.salud.hce.backend.repository;

import ec.gob.salud.hce.backend.entity.DatoGestacional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatoGestacionalRepository extends JpaRepository<DatoGestacional, Integer> {
}