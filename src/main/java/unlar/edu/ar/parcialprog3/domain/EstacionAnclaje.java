package unlar.edu.ar.parcialprog3.domain;



import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "estaciones_anclaje")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstacionAnclaje {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEstacion;
    
    @Column(nullable = false, unique = true)
    private String nombre;
    
    @Column(nullable = false)
    private String ubicacion;
    
    @Column(nullable = false)
    private Double latitud;
    
    @Column(nullable = false)
    private Double longitud;
    
    @Column(nullable = false)
    private Integer capacidadMaxima;
    
    @OneToMany(mappedBy = "estacionActual", cascade = CascadeType.ALL)
    private List<Vehiculo> vehiculosGuardados;
    
    @Column(nullable = false)
    private Boolean activa = true;
    
    public Integer getEspaciosDisponibles() {
        if (vehiculosGuardados == null) {
            return capacidadMaxima;
        }
        return capacidadMaxima - vehiculosGuardados.size();
    }
    
    public Boolean puedeGuardarVehiculo() {
        return getEspaciosDisponibles() > 0;
    }
}
