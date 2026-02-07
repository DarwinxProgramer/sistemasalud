package ec.gob.salud.hce.backend.repository;

import ec.gob.salud.hce.backend.entity.SignoVital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignoVitalRepository extends JpaRepository<SignoVital, Integer> {
    // Aquí puedes añadir consultas personalizadas como:
    // List<SignoVital> findByIdExamenFisico(Integer idExamenFisico);
}