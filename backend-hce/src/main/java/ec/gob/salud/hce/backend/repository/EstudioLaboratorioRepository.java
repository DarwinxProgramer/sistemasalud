package ec.gob.salud.hce.backend.repository;

import ec.gob.salud.hce.backend.entity.EstudioLaboratorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstudioLaboratorioRepository extends JpaRepository<EstudioLaboratorio, Integer> {
}