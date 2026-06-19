package unlar.edu.ar.parcialprog3.controller;

import unlar.edu.ar.parcialprog3.domain.Alquiler;
import unlar.edu.ar.parcialprog3.domain.Pago;
import unlar.edu.ar.parcialprog3.dto.DesbloqueoVehiculoResponse;
import unlar.edu.ar.parcialprog3.dto.FinalizacionAlquilerResponse;
import unlar.edu.ar.parcialprog3.dto.RespuestaError;
import unlar.edu.ar.parcialprog3.pattern.strategy.EstrategiaCalculoTarifa;
import unlar.edu.ar.parcialprog3.pattern.strategy.EstrategiaEstandar;
import unlar.edu.ar.parcialprog3.pattern.strategy.EstrategiaHoraPico;
import unlar.edu.ar.parcialprog3.pattern.strategy.EstrategiaTemporalClimatico;
import unlar.edu.ar.parcialprog3.service.AlquilerService;
import lombok.RequiredArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/alquileres")
@RequiredArgsConstructor
public class AlquilerController {
    
    private final AlquilerService alquilerService;
    
    /**
     * Desbloquea un vehículo e inicia un alquiler.
     * Responde con un DTO profesional en lugar de exponer las entidades JPA crudas.
     * GET /api/alquileres/desbloquear
     *
     * @param solicitud Contiene:
     *  - idUsuario: ID del usuario que solicita el viaje
     *  - patente: Placa del vehículo a desbloquear
     *  - metodoPago: Método de pago ("TARJETA_CREDITO" o "BILLETERA_VIRTUAL")
     */
    @GetMapping("/desbloquear")
    public ResponseEntity<?> desbloquearVehiculo(@RequestBody SolicitudDesbloqueoDTO solicitud) {
        try {
            // Validar solicitud
            if (solicitud.getIdUsuario() == null || solicitud.getIdUsuario() <= 0) {
                return ResponseEntity.badRequest().body(
                    crearRespuestaError("El ID del usuario es requerido")
                );
            }
            
            if (solicitud.getPatente() == null || solicitud.getPatente().isBlank()) {
                return ResponseEntity.badRequest().body(
                    crearRespuestaError("La patente del vehículo es requerida")
                );
            }
            
            if (solicitud.getMetodoPago() == null || solicitud.getMetodoPago().isBlank()) {
                return ResponseEntity.badRequest().body(
                    crearRespuestaError("El método de pago es requerido")
                );
            }
            
            // Convertir método de pago
            Pago.MetodoPago metodoPago = Pago.MetodoPago.valueOf(
                solicitud.getMetodoPago().toUpperCase().replace(" ", "_")
            );
            
            // Desbloquear vehículo (aplica patrón STATE + STRATEGY internamente)
            Alquiler alquiler = alquilerService.desbloquearVehiculo(
                solicitud.getIdUsuario(),
                solicitud.getPatente(),
                metodoPago
            );
            
            DesbloqueoVehiculoResponse respuesta = DesbloqueoVehiculoResponse.builder()
                    .exitoso(true)
                    .mensaje("Vehículo desbloqueado exitosamente")
                    .alquilerId(alquiler.getIdAlquiler())
                    .patente(alquiler.getVehiculo().getPatente())
                    .tipoVehiculo(alquiler.getVehiculo().getTipo().toString())
                    .montoInicial(alquiler.getMontoTotal())
                    .estadoVehiculo(alquiler.getVehiculo().getEstadoCiclo().toString())
                    .horaInicio(alquiler.getFechaInicio())
                    .build();

            return ResponseEntity.ok(respuesta);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                crearRespuestaError(e.getMessage())
            );
        } catch (NoSuchElementException e) {
            if (e.getMessage().contains("Usuario")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    crearRespuestaError("Usuario No Encontrado")
                );
            } else if (e.getMessage().contains("Vehículo")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    crearRespuestaError("Vehículo No Encontrado")
                );
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                crearRespuestaError("Recurso no encontrado")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                crearRespuestaError("Error interno del servidor: " + e.getMessage())
            );
        }
    }
    
    /**
     * Finaliza un alquiler activo.
     * POST /api/alquileres/{idAlquiler}/finalizar
     */
    @PostMapping("/{idAlquiler}/finalizar")
    public ResponseEntity<?> finalizarAlquiler(@PathVariable Long idAlquiler) {
        try {
            Alquiler alquiler = alquilerService.finalizarAlquiler(idAlquiler);

            long minutos = 0L;
            if (alquiler.getFechaInicio() != null && alquiler.getFechaFin() != null) {
                minutos = ChronoUnit.MINUTES.between(alquiler.getFechaInicio(), alquiler.getFechaFin());
            }

            FinalizacionAlquilerResponse respuesta = FinalizacionAlquilerResponse.builder()
                    .exitoso(true)
                    .mensaje("Alquiler finalizado exitosamente")
                    .alquilerId(alquiler.getIdAlquiler())
                    .patente(alquiler.getVehiculo().getPatente())
                    .minutosTranscurridos(minutos)
                    .montoCobrado(alquiler.getMontoPagado())
                    .descuentoAplicado(alquiler.getDescuentoAplicado())
                    .estrategiaUsada(alquilerService.obtenerInfoEstrategiaTarifa())
                    .estadoVehiculo(alquiler.getVehiculo().getEstadoCiclo().toString())
                    .horaFin(alquiler.getFechaFin())
                    .build();

            return ResponseEntity.ok(respuesta);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                crearRespuestaError(e.getMessage())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                crearRespuestaError("Error al finalizar alquiler: " + e.getMessage())
            );
        }
    }
    
    /**
     * Obtiene todos los alquileres de un usuario
     * GET /api/alquileres/usuario/{idUsuario}
     */
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<?> obtenerAlquileresPorUsuario(@PathVariable Long idUsuario) {
        try {
            var alquileres = alquilerService.obtenerAlquileresPorUsuario(idUsuario);
            return ResponseEntity.ok(Map.of(
                "alquileres", alquileres,
                "total", alquileres.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                crearRespuestaError(e.getMessage())
            );
        }
    }
    
    /**
     * Obtiene todos los alquileres activos
     * GET /api/alquileres/activos
     */
    @GetMapping("/activos")
    public ResponseEntity<?> obtenerAlquileresActivos() {
        try {
            var alquileres = alquilerService.obtenerAlquileresActivos();
            return ResponseEntity.ok(Map.of(
                "alquileres", alquileres,
                "total", alquileres.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                crearRespuestaError(e.getMessage())
            );
        }
    }

    /**
     * Cambia la estrategia de cálculo de tarifa activa en tiempo real (patrón STRATEGY).
     * POST /api/alquileres/admin/cambiar-estrategia-tarifa?estrategia=HORA_PICO
     */
    @PostMapping("/admin/cambiar-estrategia-tarifa")
    public ResponseEntity<?> cambiarEstrategia(@RequestParam String estrategia) {
        try {
            EstrategiaCalculoTarifa nueva = switch (estrategia.toUpperCase()) {
                case "HORA_PICO" -> new EstrategiaHoraPico();
                case "TEMPORAL_CLIMATICO" -> new EstrategiaTemporalClimatico();
                default -> new EstrategiaEstandar();
            };

            alquilerService.cambiarEstrategiaTarifa(nueva);

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("exitoso", true);
            respuesta.put("mensaje", "Estrategia de tarifa actualizada");
            respuesta.put("estrategiaActiva", alquilerService.obtenerInfoEstrategiaTarifa());

            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                RespuestaError.builder()
                        .exitoso(false)
                        .codigo("500")
                        .mensaje("Error Interno")
                        .detalles("Error al cambiar estrategia: " + e.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build()
            );
        }
    }
    
    // DTOs
    @Data
    public static class SolicitudDesbloqueoDTO {
        private Long idUsuario;
        private String patente;
        private String metodoPago;
    }
    
    // Método auxiliar para crear respuesta de error
    private Map<String, Object> crearRespuestaError(String mensaje) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("exitoso", false);
        respuesta.put("mensaje", mensaje);
        return respuesta;
    }
}
