package unlar.edu.ar.parcialprog3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO para respuestas genéricas de error.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RespuestaError {
    private Boolean exitoso;
    private String codigo;
    private String mensaje;
    private String detalles;
    private LocalDateTime timestamp;
}
