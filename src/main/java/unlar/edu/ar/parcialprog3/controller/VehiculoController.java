package unlar.edu.ar.parcialprog3.controller;



import com.ecoride.domain.Vehiculo;
import com.ecoride.dto.*;
import com.ecoride.service.VehiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vehiculos")
@RequiredArgsConstructor
public class VehiculoController {
    
    private final VehiculoService vehiculoService;
    
    /**
     * Crea un nuevo vehículo
     * POST /api/vehiculos
     */
    @PostMapping
    public ResponseEntity<?> crearVehiculo(@RequestBody Vehiculo vehiculo) {
        try {
            Vehiculo nuevoVehiculo = vehiculoService.crearVehiculo(vehiculo);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoVehiculo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(crearRespuestaError(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError(e.getMessage()));
        }
    }
    
    /**
     * Obtiene un vehículo por patente (consulta directa a base de datos)
     * GET /api/vehiculos/patente/{patente}
     */
    @GetMapping("/patente/{patente}")
    public ResponseEntity<?> obtenerPorPatente(@PathVariable String patente) {
        try {
            Vehiculo vehiculo = vehiculoService.obtenerPorPatente(patente);
            return ResponseEntity.ok(vehiculo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError("Vehículo no encontrado"));
        }
    }

    /**
     * Obtiene un vehículo por patente usando el caché O(1) (búsqueda optimizada).
     * GET /api/vehiculos/patente/{patente}/rapido
     */
    @GetMapping("/patente/{patente}/rapido")
    public ResponseEntity<?> obtenerPorPatenteRapido(@PathVariable String patente) {
        Vehiculo vehiculo = vehiculoService.obtenerPorPatenteRapido(patente);
        if (vehiculo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError("Vehículo no encontrado"));
        }
        return ResponseEntity.ok(vehiculo);
    }
    
    /**
     * Obtiene todos los vehículos disponibles en una estación
     * GET /api/vehiculos/estacion/{idEstacion}
     */
    @GetMapping("/estacion/{idEstacion}")
    public ResponseEntity<?> obtenerDisponiblesEnEstacion(@PathVariable Long idEstacion) {
        try {
            var vehiculos = vehiculoService.obtenerDisponiblesEnEstacion(idEstacion);
            return ResponseEntity.ok(Map.of(
                    "vehiculos", vehiculos,
                    "total", vehiculos.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError(e.getMessage()));
        }
    }
    
    /**
     * Obtiene todos los vehículos de un tipo
     * GET /api/vehiculos/tipo/{tipo}
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<?> obtenerPorTipo(@PathVariable String tipo) {
        try {
            Vehiculo.TipoVehiculo tipoVehiculo = Vehiculo.TipoVehiculo.valueOf(tipo.toUpperCase());
            var vehiculos = vehiculoService.obtenerPorTipo(tipoVehiculo);
            return ResponseEntity.ok(Map.of(
                    "vehiculos", vehiculos,
                    "total", vehiculos.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(crearRespuestaError("Tipo de vehículo inválido"));
        }
    }
    
    /**
     * Actualiza el nivel de batería
     * PATCH /api/vehiculos/{idVehiculo}/bateria
     */
    @PatchMapping("/{idVehiculo}/bateria")
    public ResponseEntity<?> actualizarBateria(
            @PathVariable Long idVehiculo,
            @RequestParam Integer nuevoNivel) {
        try {
            Vehiculo vehiculo = vehiculoService.actualizarBateria(idVehiculo, nuevoNivel);
            return ResponseEntity.ok(vehiculo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(crearRespuestaError(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError("Vehículo no encontrado"));
        }
    }
    
    /**
     * Mueve un vehículo a una nueva estación
     * PUT /api/vehiculos/{idVehiculo}/estacion
     */
    @PutMapping("/{idVehiculo}/estacion")
    public ResponseEntity<?> moverVehiculo(
            @PathVariable Long idVehiculo,
            @RequestParam Long idEstacionNueva) {
        try {
            Vehiculo vehiculo = vehiculoService.moverVehiculo(idVehiculo, idEstacionNueva);
            return ResponseEntity.ok(vehiculo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(crearRespuestaError(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError(e.getMessage()));
        }
    }
    
    /**
     * Obtiene vehículos EN_ESPERA con batería suficiente
     * GET /api/vehiculos/disponibles
     */
    @GetMapping("/disponibles")
    public ResponseEntity<?> obtenerDisponibles(
            @RequestParam(defaultValue = "15") Integer nivelMinimoBateria) {
        try {
            var vehiculos = vehiculoService.obtenerDisponiblesConBateria(nivelMinimoBateria);
            return ResponseEntity.ok(Map.of(
                    "vehiculos", vehiculos,
                    "total", vehiculos.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError(e.getMessage()));
        }
    }

    /**
     * Vehículos ordenados por batería ascendente (prioridad de mantenimiento).
     * GET /api/vehiculos/prioridad-natural
     */
    @GetMapping("/prioridad-natural")
    public ResponseEntity<?> obtenerVehiculosPrioridadNatural() {
        try {
            List<Vehiculo> ordenados = vehiculoService.obtenerVehiculosOrdenadosPorBateria();
            ListaVehiculosOrdenada respuesta = construirListaOrdenada(
                    ordenados,
                    "PRIORIDAD_NATURAL_BATERIA",
                    "Vehículos ordenados por batería (menor a mayor). Útil para el equipo de mantenimiento."
            );
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaErrorDetallada("Error Interno", "Error al obtener reportes: " + e.getMessage()));
        }
    }

    /**
     * Vehículos ordenados por tarifa descendente (criterio de finanzas).
     * GET /api/vehiculos/tarifa-descendente
     */
    @GetMapping("/tarifa-descendente")
    public ResponseEntity<?> obtenerVehiculosTarifaDescendente() {
        try {
            List<Vehiculo> ordenados = vehiculoService.obtenerVehiculosOrdenadosPorTarifa();
            ListaVehiculosOrdenada respuesta = construirListaOrdenada(
                    ordenados,
                    "TARIFA_DESCENDENTE",
                    "Vehículos ordenados por tarifa (mayor a menor). Útil para el equipo de finanzas."
            );
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaErrorDetallada("Error Interno", "Error al obtener reportes: " + e.getMessage()));
        }
    }

    /**
     * Deduplica una lista de alertas GPS reportadas por los vehículos (O(n) con HashSet).
     * POST /api/vehiculos/gps/deduplicar
     */
    @PostMapping("/gps/deduplicar")
    public ResponseEntity<?> deduplicarAlertasGPS(@RequestBody List<AlertaGPSDTO> alertas) {
        try {
            ResultadoDeduplicacionGPS resultado = vehiculoService.deduplicarAlertasGPS(alertas);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaErrorDetallada("Error Interno", "Error al deduplicar alertas: " + e.getMessage()));
        }
    }

    private ListaVehiculosOrdenada construirListaOrdenada(List<Vehiculo> vehiculos, String criterio, String descripcion) {
        List<VehiculoReporteDTO> dtos = vehiculos.stream()
                .map(v -> VehiculoReporteDTO.builder()
                        .id(v.getIdVehiculo())
                        .patente(v.getPatente())
                        .tipo(v.getTipo().toString())
                        .tarifa(v.getValorTarifa())
                        .nivelBateria(v.getNivelBateria())
                        .estado(v.getEstadoCiclo().toString())
                        .estacion(v.getEstacionActual() != null ? v.getEstacionActual().getNombre() : "N/A")
                        .build())
                .collect(Collectors.toList());

        return ListaVehiculosOrdenada.builder()
                .criterioOrdenamiento(criterio)
                .vehiculos(dtos)
                .total(dtos.size())
                .descripcion(descripcion)
                .build();
    }
    
    private Map<String, Object> crearRespuestaError(String mensaje) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("exitoso", false);
        respuesta.put("mensaje", mensaje);
        return respuesta;
    }

    private RespuestaError crearRespuestaErrorDetallada(String codigo, String detalles) {
        return RespuestaError.builder()
                .exitoso(false)
                .codigo(codigo)
                .mensaje(detalles)
                .detalles(detalles)
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }
}
