package ec.gob.salud.hce.backend.repository;

import ec.gob.salud.hce.backend.entity.ComplicacionPerinatal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ComplicacionPerinatalRepository extends JpaRepository<ComplicacionPerinatal, Integer> {
    List<ComplicacionPerinatal> findByIdDatoGestacional(Integer idDatoGestacional);
}