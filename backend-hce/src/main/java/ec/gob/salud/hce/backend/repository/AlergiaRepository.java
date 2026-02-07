package ec.gob.salud.hce.backend.repository;
import ec.gob.salud.hce.backend.entity.Alergia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlergiaRepository extends JpaRepository<Alergia, Long> {
    // Puedes agregar métodos personalizados aquí si los necesitas
}