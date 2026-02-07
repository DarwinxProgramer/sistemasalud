package ec.gob.salud.hce.backend.repository;

import ec.gob.salud.hce.backend.entity.PlanTerapeutico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanTerapeuticoRepository extends JpaRepository<PlanTerapeutico, Integer> {
}