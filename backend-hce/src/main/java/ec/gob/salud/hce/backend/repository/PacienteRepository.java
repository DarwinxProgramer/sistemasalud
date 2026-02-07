package ec.gob.salud.hce.backend.repository;

import ec.gob.salud.hce.backend.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List; // <--- AGREGA ESTO
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Integer> {
    List<Paciente> findByCedula(String cedula);

    // Cargar la relación pacientesTutores eagerly para sync down
    @EntityGraph(attributePaths = { "pacientesTutores", "pacientesTutores.tutor" })
    @Query("SELECT p FROM Paciente p")
    List<Paciente> findAllWithTutores();

    // Método auxiliar para buscar por UUID (utilizado en sincronización)
    Optional<Paciente> findByUuidOffline(String uuidOffline);

    // Tu código va aquí dentro:
    @Query("SELECT p FROM Paciente p WHERE p.cedula LIKE %:filtro% OR p.apellidoPaterno LIKE %:filtro% OR p.apellidoMaterno LIKE %:filtro%")
    List<Paciente> buscarPorCriterio(@Param("filtro") String filtro);
}