package ec.gob.salud.hce.backend.security;

import ec.gob.salud.hce.backend.entity.Usuario;
import ec.gob.salud.hce.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Buscamos el usuario en TU base de datos (tabla personal)
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // 2. Convertimos tu campo 'cargo' (ej: "medico") en una Autoridad (ej: "ROLE_MEDICO")
        // Spring Security espera que los roles empiecen con "ROLE_"
        String nombreRol = "ROLE_" + usuario.getCargo().toUpperCase();
        GrantedAuthority authority = new SimpleGrantedAuthority(nombreRol);

        // 3. Devolvemos el objeto User oficial de Spring Security
        return new User(
                usuario.getUsername(),
                usuario.getPassword(), // Aquí va el hash que guardamos en la BD
                Collections.singletonList(authority) // Lista con el rol
        );
    }
}