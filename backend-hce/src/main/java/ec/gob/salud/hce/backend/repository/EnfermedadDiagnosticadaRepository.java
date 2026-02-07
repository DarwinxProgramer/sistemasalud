package ec.gob.salud.hce.backend.repository;

import ec.gob.salud.hce.backend.entity.EnfermedadDiagnosticada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnfermedadDiagnosticadaRepository extends JpaRepository<EnfermedadDiagnosticada, Integer> {
}