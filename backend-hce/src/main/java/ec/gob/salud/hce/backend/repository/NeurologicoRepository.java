package ec.gob.salud.hce.backend.repository;

import ec.gob.salud.hce.backend.entity.Neurologico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NeurologicoRepository extends JpaRepository<Neurologico, Integer> {
}