package unlar.edu.ar.parcialprog3.controller;



import com.ecoride.domain.EstacionAnclaje;
import com.ecoride.service.EstacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/estaciones")
@RequiredArgsConstructor
public class EstacionController {
    
    private final EstacionService estacionService;
    
    /**
     * Crea una nueva estación de anclaje
     * POST /api/estaciones
     */
    @PostMapping
    public ResponseEntity<?> crearEstacion(@RequestBody EstacionAnclaje estacion) {
        try {
            EstacionAnclaje nuevaEstacion = estacionService.crearEstacion(estacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaEstacion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(crearRespuestaError(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError(e.getMessage()));
        }
    }
    
    /**
     * Obtiene una estación por ID
     * GET /api/estaciones/{idEstacion}
     */
    @GetMapping("/{idEstacion}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long idEstacion) {
        try {
            EstacionAnclaje estacion = estacionService.obtenerPorId(idEstacion);
            return ResponseEntity.ok(estacion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError("Estación no encontrada"));
        }
    }
    
    /**
     * Obtiene una estación por nombre
     * GET /api/estaciones/nombre/{nombre}
     */
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<?> obtenerPorNombre(@PathVariable String nombre) {
        try {
            EstacionAnclaje estacion = estacionService.obtenerPorNombre(nombre);
            return ResponseEntity.ok(estacion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError("Estación no encontrada"));
        }
    }
    
    /**
     * Obtiene todas las estaciones activas
     * GET /api/estaciones/activas
     */
    @GetMapping("/activas")
    public ResponseEntity<?> obtenerEstacionesActivas() {
        try {
            var estaciones = estacionService.obtenerEstacionesActivas();
            return ResponseEntity.ok(Map.of(
                    "estaciones", estaciones,
                    "total", estaciones.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError(e.getMessage()));
        }
    }
    
    /**
     * Obtiene los espacios disponibles en una estación
     * GET /api/estaciones/{idEstacion}/espacios
     */
    @GetMapping("/{idEstacion}/espacios")
    public ResponseEntity<?> obtenerEspaciosDisponibles(@PathVariable Long idEstacion) {
        try {
            Integer espacios = estacionService.obtenerEspaciosDisponibles(idEstacion);
            return ResponseEntity.ok(Map.of(
                    "espaciosDisponibles", espacios
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError("Estación no encontrada"));
        }
    }
    
    /**
     * Verifica si una estación puede guardar un vehículo
     * GET /api/estaciones/{idEstacion}/puede-guardar
     */
    @GetMapping("/{idEstacion}/puede-guardar")
    public ResponseEntity<?> puedeGuardarVehiculo(@PathVariable Long idEstacion) {
        try {
            Boolean puedeGuardar = estacionService.puedeGuardarVehiculo(idEstacion);
            return ResponseEntity.ok(Map.of(
                    "puedeGuardar", puedeGuardar
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError("Estación no encontrada"));
        }
    }
    
    /**
     * Desactiva una estación
     * DELETE /api/estaciones/{idEstacion}
     */
    @DeleteMapping("/{idEstacion}")
    public ResponseEntity<?> desactivarEstacion(@PathVariable Long idEstacion) {
        try {
            EstacionAnclaje estacion = estacionService.desactivarEstacion(idEstacion);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Estación desactivada exitosamente",
                    "estacion", estacion
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError("Estación no encontrada"));
        }
    }
    
    /**
     * Actualiza la capacidad de una estación
     * PATCH /api/estaciones/{idEstacion}/capacidad
     */
    @PatchMapping("/{idEstacion}/capacidad")
    public ResponseEntity<?> actualizarCapacidad(
            @PathVariable Long idEstacion,
            @RequestParam Integer nuevaCapacidad) {
        try {
            EstacionAnclaje estacion = estacionService.actualizarCapacidad(idEstacion, nuevaCapacidad);
            return ResponseEntity.ok(estacion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(crearRespuestaError(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError("Estación no encontrada"));
        }
    }
    
    private Map<String, Object> crearRespuestaError(String mensaje) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("exitoso", false);
        respuesta.put("mensaje", mensaje);
        return respuesta;
    }
}
