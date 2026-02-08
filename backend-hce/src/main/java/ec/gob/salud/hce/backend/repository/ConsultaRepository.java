package ec.gob.salud.hce.backend.repository;

import ec.gob.salud.hce.backend.entity.Consulta;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    List<Consulta> findByPaciente_IdPaciente(Integer idPaciente);

    List<Consulta> findByPaciente_IdPacienteOrderByFechaConsultaDesc(Integer idPaciente);

    Optional<Consulta> findByUuidOffline(String uuidOffline);

    // Cargar consultas con planes y estudios para sync down/listados completos
    @EntityGraph(attributePaths = { "planes", "estudios" })
    @Query("SELECT c FROM Consulta c WHERE c.paciente.idPaciente = :idPaciente ORDER BY c.fechaConsulta DESC")
    List<Consulta> findByIdPacienteWithDetails(@Param("idPaciente") Integer idPaciente);

    @EntityGraph(attributePaths = { "planes", "estudios" })
    @Query("SELECT c FROM Consulta c")
    List<Consulta> findAllWithDetails();
}