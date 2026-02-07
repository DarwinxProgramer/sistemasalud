package ec.gob.salud.hce.backend.repository;

import ec.gob.salud.hce.backend.entity.CardioPulmonar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardioPulmonarRepository extends JpaRepository<CardioPulmonar, Integer> {
}