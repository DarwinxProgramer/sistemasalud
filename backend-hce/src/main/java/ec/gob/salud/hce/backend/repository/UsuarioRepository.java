package ec.gob.salud.hce.backend.repository;

import ec.gob.salud.hce.backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// CAMBIO IMPORTANTE: De <Usuario, Long> a <Usuario, Integer>
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByUsername(String username);
}