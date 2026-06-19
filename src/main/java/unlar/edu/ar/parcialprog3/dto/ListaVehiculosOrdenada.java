package unlar.edu.ar.parcialprog3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * DTO para listar vehículos con criterio de ordenamiento.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListaVehiculosOrdenada {
    private String criterioOrdenamiento;
    private List<VehiculoReporteDTO> vehiculos;
    private Integer total;
    private String descripcion;
}
