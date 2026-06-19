package unlar.edu.ar.parcialprog3.controller;



import com.ecoride.domain.Usuario;
import com.ecoride.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    
    private final UsuarioService usuarioService;
    
    /**
     * Registra un nuevo usuario
     * POST /api/usuarios
     */
    @PostMapping
    public ResponseEntity<?> registrarUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario nuevoUsuario = usuarioService.registrarUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(crearRespuestaError(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError(e.getMessage()));
        }
    }
    
    /**
     * Obtiene un usuario por ID
     * GET /api/usuarios/{idUsuario}
     */
    @GetMapping("/{idUsuario}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long idUsuario) {
        try {
            Usuario usuario = usuarioService.obtenerPorId(idUsuario);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError("Usuario no encontrado"));
        }
    }
    
    /**
     * Obtiene un usuario por correo
     * GET /api/usuarios/correo/{correo}
     */
    @GetMapping("/correo/{correo}")
    public ResponseEntity<?> obtenerPorCorreo(@PathVariable String correo) {
        try {
            Usuario usuario = usuarioService.obtenerPorCorreo(correo);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError("Usuario no encontrado"));
        }
    }
    
    /**
     * Obtiene todos los usuarios premium
     * GET /api/usuarios/premium
     */
    @GetMapping("/premium")
    public ResponseEntity<?> obtenerUsuariosPremium() {
        try {
            var usuarios = usuarioService.obtenerUsuariosPremium();
            return ResponseEntity.ok(Map.of(
                    "usuarios", usuarios,
                    "total", usuarios.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError(e.getMessage()));
        }
    }
    
    /**
     * Obtiene todos los usuarios regulares
     * GET /api/usuarios/regulares
     */
    @GetMapping("/regulares")
    public ResponseEntity<?> obtenerUsuariosRegulares() {
        try {
            var usuarios = usuarioService.obtenerUsuariosRegulares();
            return ResponseEntity.ok(Map.of(
                    "usuarios", usuarios,
                    "total", usuarios.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError(e.getMessage()));
        }
    }
    
    /**
     * Obtiene todos los usuarios activos
     * GET /api/usuarios/activos
     */
    @GetMapping("/activos")
    public ResponseEntity<?> obtenerUsuariosActivos() {
        try {
            var usuarios = usuarioService.obtenerUsuariosActivos();
            return ResponseEntity.ok(Map.of(
                    "usuarios", usuarios,
                    "total", usuarios.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError(e.getMessage()));
        }
    }
    
    /**
     * Actualiza el tipo de usuario (REGULAR o PREMIUM)
     * PATCH /api/usuarios/{idUsuario}/tipo
     */
    @PatchMapping("/{idUsuario}/tipo")
    public ResponseEntity<?> actualizarTipoUsuario(
            @PathVariable Long idUsuario,
            @RequestParam String nuevoTipo) {
        try {
            Usuario.TipoUsuario tipo = Usuario.TipoUsuario.valueOf(nuevoTipo.toUpperCase());
            Usuario usuario = usuarioService.actualizarTipoUsuario(idUsuario, tipo);
            return ResponseEntity.ok(usuario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(crearRespuestaError("Tipo de usuario inválido"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError("Usuario no encontrado"));
        }
    }
    
    /**
     * Desactiva un usuario
     * DELETE /api/usuarios/{idUsuario}
     */
    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<?> desactivarUsuario(@PathVariable Long idUsuario) {
        try {
            Usuario usuario = usuarioService.desactivarUsuario(idUsuario);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Usuario desactivado exitosamente",
                    "usuario", usuario
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError("Usuario no encontrado"));
        }
    }
    
    private Map<String, Object> crearRespuestaError(String mensaje) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("exitoso", false);
        respuesta.put("mensaje", mensaje);
        return respuesta;
    }
}
