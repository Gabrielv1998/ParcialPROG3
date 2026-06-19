package unlar.edu.ar.parcialprog3.domain;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;
    
    @Column(nullable = false)
    private String nombreCompleto;
    
    @Column(nullable = false, unique = true)
    private String correo;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario tipoUsuario;
    
    @Column(nullable = false)
    private Double descuentoAplicable;
    
    @Column(nullable = false)
    private Boolean activo = true;
    
    public enum TipoUsuario {
        REGULAR,
        PREMIUM
    }
    
    public Double getDescuentoAplicable() {
        return tipoUsuario == TipoUsuario.PREMIUM ? 15.0 : 0.0;
    }
}
