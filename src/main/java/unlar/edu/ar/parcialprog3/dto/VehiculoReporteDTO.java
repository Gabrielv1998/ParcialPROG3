package unlar.edu.ar.parcialprog3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para vehículo en reportes (sin exponer detalles internos de la entidad JPA).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehiculoReporteDTO {
    private Long id;
    private String patente;
    private String tipo;
    private Double tarifa;
    private Integer nivelBateria;
    private String estado;
    private String estacion;
}
