package unlar.edu.ar.parcialprog3.pattern.state;



/**
 * Estado: EN ESPERA
 * El vehículo está descansando en la estación, listo para ser alquilado.
 */
public class EnEspera implements EstadoVehiculo {

    @Override
    public boolean puedeIniciarViaje() {
        return true;  // ✅ Puede iniciar viaje
    }

    @Override
    public boolean puedeIrReparacion() {
        return true;  // ✅ Puede ir a reparación
    }

    @Override
    public boolean puedeFinalizarViaje() {
        return false; // ❌ No hay viaje activo
    }

    @Override
    public String getNombre() {
        return "EN_ESPERA";
    }

    @Override
    public String getDescripcion() {
        return "El vehículo está listo en la estación para ser alquilado";
    }
}
