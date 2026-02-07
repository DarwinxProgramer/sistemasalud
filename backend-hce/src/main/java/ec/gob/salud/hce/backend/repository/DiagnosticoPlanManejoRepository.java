package ec.gob.salud.hce.backend.repository;

import ec.gob.salud.hce.backend.entity.DiagnosticoPlanManejo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiagnosticoPlanManejoRepository extends JpaRepository<DiagnosticoPlanManejo, Integer> {
}