package unlar.edu.ar.parcialprog3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO para alertas de posición GPS reportadas por los vehículos.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertaGPSDTO {
    private String vehiculoId;
    private Double latitud;
    private Double longitud;
    private LocalDateTime timestamp;
}
