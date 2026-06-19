package unlar.edu.ar.parcialprog3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * DTO para el resultado de la deduplicación de alertas GPS.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoDeduplicacionGPS {
    private Integer totalOriginal;
    private Integer totalDuplicados;
    private Integer totalUnicos;
    private List<AlertaGPSDTO> alertasLimpias;
    private String algoritmoUsado;
    private Long tiempoEjecucionMs;
}
