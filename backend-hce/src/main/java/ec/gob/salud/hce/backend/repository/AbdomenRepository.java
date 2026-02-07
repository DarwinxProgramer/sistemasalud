package ec.gob.salud.hce.backend.repository;

import ec.gob.salud.hce.backend.entity.Abdomen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbdomenRepository extends JpaRepository<Abdomen, Integer> {
}