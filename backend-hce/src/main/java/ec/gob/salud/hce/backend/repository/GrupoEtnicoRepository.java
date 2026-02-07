package ec.gob.salud.hce.backend.repository;

import ec.gob.salud.hce.backend.entity.GrupoEtnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrupoEtnicoRepository extends JpaRepository<GrupoEtnico, Integer> {
}