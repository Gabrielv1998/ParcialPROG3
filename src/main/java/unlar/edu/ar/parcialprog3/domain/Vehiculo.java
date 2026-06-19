package unlar.edu.ar.parcialprog3.domain;



import unlar.edu.ar.parcialprog3.pattern.state.*;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad Vehículo.
 *
 * El ciclo de vida del vehículo se controla mediante el patrón STATE
 * (ver paquete com.ecoride.pattern.state) en lugar de un simple booleano
 * "disponible". Esto permite representar tres estados reales del negocio
 * (EN_ESPERA, EN_VIAJE, EN_REPARACION) y validar correctamente qué
 * transiciones son posibles desde cada uno.
 */
@Entity
@Table(name = "vehiculos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVehiculo;

    @Column(nullable = false, unique = true)
    private String patente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoVehiculo tipo;

    @Column(nullable = false)
    private Double valorTarifa;

    @Column(nullable = false)
    private Integer nivelBateria;

    // ESTADO ACTUAL DEL CICLO DE VIDA (reemplaza al antiguo campo "disponible")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCicloVida estadoCiclo = EstadoCicloVida.EN_ESPERA;

    // Para bicicletas: capacidad en cm³
    // Para monopatines: valor booleano si tiene sistema amortiguación
    @Column(name = "atributo_especifico")
    private String atributoEspecifico;

    @ManyToOne
    @JoinColumn(name = "id_estacion")
    private EstacionAnclaje estacionActual;

    public enum TipoVehiculo {
        MONOPATINES,
        BICICLETAS_ELECTRICAS
    }

    /**
     * Estados del ciclo de vida del vehículo.
     */
    public enum EstadoCicloVida {
        EN_ESPERA,      // En la estación, listo para alquilar
        EN_VIAJE,       // Siendo conducido por un usuario
        EN_REPARACION   // Tiene fallas, en mantenimiento
    }

    /**
     * Obtiene el objeto Estado (patrón STATE) correspondiente al ciclo de vida actual.
     */
    public EstadoVehiculo obtenerEstado() {
        switch (this.estadoCiclo) {
            case EN_ESPERA:
                return new EnEspera();
            case EN_VIAJE:
                return new EnViaje();
            case EN_REPARACION:
                return new EnReparacion();
            default:
                return new EnEspera();
        }
    }

    /**
     * Intenta iniciar un viaje (respeta transiciones válidas y batería mínima).
     */
    public Boolean intentarIniciarViaje() {
        if (obtenerEstado().puedeIniciarViaje() && validarBateria()) {
            this.estadoCiclo = EstadoCicloVida.EN_VIAJE;
            return true;
        }
        return false;
    }

    /**
     * Intenta finalizar el viaje (respeta transiciones válidas).
     */
    public Boolean intentarFinalizarViaje() {
        if (obtenerEstado().puedeFinalizarViaje()) {
            this.estadoCiclo = EstadoCicloVida.EN_ESPERA;
            return true;
        }
        return false;
    }

    /**
     * Intenta enviar el vehículo a reparación (respeta transiciones válidas).
     */
    public Boolean intentarIrReparacion() {
        if (obtenerEstado().puedeIrReparacion()) {
            this.estadoCiclo = EstadoCicloVida.EN_REPARACION;
            return true;
        }
        return false;
    }

    /**
     * Valida que la batería sea suficiente (mínimo 15%).
     */
    public Boolean validarBateria() {
        return nivelBateria >= 15;
    }

    /**
     * Verifica si el vehículo puede ser alquilado en este momento.
     */
    public Boolean puedeSerAlquilado() {
        return this.estadoCiclo == EstadoCicloVida.EN_ESPERA && validarBateria();
    }
}
