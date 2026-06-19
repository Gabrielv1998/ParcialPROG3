package unlar.edu.ar.parcialprog3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO para la respuesta del desbloqueo de un vehículo.
 * Expone únicamente información relevante al cliente, sin filtrar
 * detalles internos de las entidades JPA.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DesbloqueoVehiculoResponse {
    private Boolean exitoso;
    private String mensaje;
    private Long alquilerId;
    private String patente;
    private String tipoVehiculo;
    private Double montoInicial;
    private String estadoVehiculo;
    private LocalDateTime horaInicio;
}
