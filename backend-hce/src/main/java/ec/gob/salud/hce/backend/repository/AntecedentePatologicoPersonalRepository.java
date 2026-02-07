package ec.gob.salud.hce.backend.repository;

import ec.gob.salud.hce.backend.entity.AntecedentePatologicoPersonal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AntecedentePatologicoPersonalRepository extends JpaRepository<AntecedentePatologicoPersonal, Integer> {
}