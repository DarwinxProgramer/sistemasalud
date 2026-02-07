package ec.gob.salud.hce.backend.repository;

import ec.gob.salud.hce.backend.entity.Canton;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CantonRepository extends JpaRepository<Canton, Long> {
    // Este método busca automáticamente todos los cantones que pertenecen a una provincia
    List<Canton> findByProvinciaId(Long provinciaId);
}