package ec.gob.salud.hce.backend.repository;
import java.util.List;
import ec.gob.salud.hce.backend.entity.AntecedentePerinatal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AntecedentePerinatalRepository extends JpaRepository<AntecedentePerinatal, Integer> {
    List<AntecedentePerinatal> findByPaciente_IdPaciente(Integer idPaciente);
}