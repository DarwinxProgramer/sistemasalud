package ec.gob.salud.hce.backend.controller;

import ec.gob.salud.hce.backend.entity.Usuario;
import ec.gob.salud.hce.backend.entity.Consulta;
import ec.gob.salud.hce.backend.repository.UsuarioRepository;
import ec.gob.salud.hce.backend.repository.ConsultaRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
// Nota: El CORS se maneja globalmente en tu WebConfig para evitar conflictos con allowCredentials
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final ConsultaRepository consultaRepository;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Usuario usuarioReal = usuarioRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("mensaje", "Login exitoso");
            response.put("username", usuarioReal.getUsername());
            response.put("nombres", usuarioReal.getNombres());
            response.put("apellidos", usuarioReal.getApellidos());
            response.put("cargo", usuarioReal.getCargo());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Credenciales incorrectas");
            return ResponseEntity.status(401).body(errorResponse);
        }
    }

    /**
     * Endpoint para el historial de actividad.
     * Mapea las consultas registradas en la base de datos MySQL.
     */
    @GetMapping("/historial")
    public ResponseEntity<?> getHistorial() {
        try {
            List<Map<String, Object>> historial = consultaRepository.findAll().stream().map(c -> {
                Map<String, Object> actividad = new HashMap<>();
                
                // Usamos idConsulta definido en la entidad
                actividad.put("id", c.getIdConsulta());
                
                // Extraemos el médico directamente del String 'usuarioMedico'
                actividad.put("usuarioNombre", c.getUsuarioMedico() != null ? c.getUsuarioMedico() : "Médico no asignado");
                
                actividad.put("accion", "CONSULTA REGISTRADA"); 
                
                // Validación del paciente para evitar el error 500 si los datos son nulos
                if (c.getPaciente() != null) {
                    // Se asume que Paciente tiene getNombres() y getApellidos() basados en errores previos
                    String nombrePac = c.getPaciente().getPrimerNombre() + " " + c.getPaciente().getApellidoPaterno();
                    actividad.put("pacienteNombre", nombrePac);
                } else {
                    actividad.put("pacienteNombre", "Paciente no identificado");
                }
                
                // Fecha capturada por @PrePersist en la entidad
                actividad.put("fechaHora", c.getFechaCreacion());
                
                return actividad;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(historial);
        } catch (Exception e) {
            // Imprime el error en la consola de Spring Boot para depuración
            e.printStackTrace(); 
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", "Error interno al procesar el historial");
            errorMap.put("details", e.getMessage());
            return ResponseEntity.status(500).body(errorMap);
        }
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }
}