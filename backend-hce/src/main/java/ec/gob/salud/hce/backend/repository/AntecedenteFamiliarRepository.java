package ec.gob.salud.hce.backend.repository;

import ec.gob.salud.hce.backend.entity.AntecedenteFamiliar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AntecedenteFamiliarRepository extends JpaRepository<AntecedenteFamiliar, Integer> {
    // Esencial para la sincronización sin duplicados
    Optional<AntecedenteFamiliar> findByUuidOffline(String uuidOffline);
}