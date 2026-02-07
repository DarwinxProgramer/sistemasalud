package ec.gob.salud.hce.backend.repository;

import ec.gob.salud.hce.backend.entity.HitoDesarrollo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HitoDesarrolloRepository extends JpaRepository<HitoDesarrollo, Integer> {
}