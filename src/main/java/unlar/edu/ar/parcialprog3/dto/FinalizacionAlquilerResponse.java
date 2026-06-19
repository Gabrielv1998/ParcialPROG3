package unlar.edu.ar.parcialprog3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO para la respuesta de finalización de un alquiler.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinalizacionAlquilerResponse {
    private Boolean exitoso;
    private String mensaje;
    private Long alquilerId;
    private String patente;
    private Long minutosTranscurridos;
    private Double montoCobrado;
    private Double descuentoAplicado;
    private String estrategiaUsada;
    private String estadoVehiculo;
    private LocalDateTime horaFin;
}
