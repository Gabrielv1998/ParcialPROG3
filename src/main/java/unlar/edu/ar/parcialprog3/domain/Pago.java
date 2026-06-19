package unlar.edu.ar.parcialprog3.domain;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pago {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPago;
    
    @OneToOne
    @JoinColumn(name = "id_alquiler", nullable = false)
    private Alquiler alquiler;
    
    @Column(nullable = false)
    private Double monto;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetodoPago metodoPago;
    
    @Column(nullable = false)
    private LocalDateTime fechaPago;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPago estado;
    
    @Column(length = 500)
    private String descripcion;
    
    public enum MetodoPago {
        TARJETA_CREDITO("Tarjeta de Crédito"),
        BILLETERA_VIRTUAL("Billetera Virtual");
        
        private final String descripcion;
        
        MetodoPago(String descripcion) {
            this.descripcion = descripcion;
        }
        
        public String getDescripcion() {
            return descripcion;
        }
    }
    
    public enum EstadoPago {
        PENDIENTE,
        EXITOSO,
        FALLIDO
    }
}
