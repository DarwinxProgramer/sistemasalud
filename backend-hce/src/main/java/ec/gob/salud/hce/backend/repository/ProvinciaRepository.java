package ec.gob.salud.hce.backend.repository;

import ec.gob.salud.hce.backend.entity.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinciaRepository extends JpaRepository<Provincia, Long> {
    // Al extender JpaRepository, ya tienes métodos como findAll() y findById()
}