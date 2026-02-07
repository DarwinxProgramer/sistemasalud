package ec.gob.salud.hce.backend.repository;

import ec.gob.salud.hce.backend.entity.Parroquia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ParroquiaRepository extends JpaRepository<Parroquia, Long> {
    // Filtra las parroquias que pertenecen a un cantón específico
    List<Parroquia> findByCantonId(Long cantonId);
}