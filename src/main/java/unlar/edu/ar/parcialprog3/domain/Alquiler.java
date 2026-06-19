package unlar.edu.ar.parcialprog3.domain;



import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alquileres")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alquiler {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAlquiler;
    
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
    
    @ManyToOne
    @JoinColumn(name = "id_vehiculo", nullable = false)
    private Vehiculo vehiculo;
    
    @ManyToOne
    @JoinColumn(name = "id_estacion_inicio", nullable = false)
    private EstacionAnclaje estacionInicio;
    
    @ManyToOne
    @JoinColumn(name = "id_estacion_fin")
    private EstacionAnclaje estacionFin;
    
    @Column(nullable = false)
    private LocalDateTime fechaInicio;
    
    @Column
    private LocalDateTime fechaFin;
    
    @Column(nullable = false)
    private Double montoTotal;
    
    @Column(nullable = false)
    private Double descuentoAplicado = 0.0;
    
    @Column(nullable = false)
    private Double montoPagado = 0.0;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoAlquiler estado;
    
    @OneToOne(mappedBy = "alquiler")
    private Pago pago;
    
    public enum EstadoAlquiler {
        ACTIVO,
        FINALIZADO,
        CANCELADO
    }
    
    public Double calcularMontoDescuentado() {
        Double descuento = montoTotal * (descuentoAplicado / 100);
        return montoTotal - descuento;
    }
}
