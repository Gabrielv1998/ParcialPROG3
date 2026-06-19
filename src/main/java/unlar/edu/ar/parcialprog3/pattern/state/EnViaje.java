package unlar.edu.ar.parcialprog3.pattern.state;



/**
 * Estado: EN VIAJE
 * El vehículo está siendo conducido por un usuario.
 */
public class EnViaje implements EstadoVehiculo {

    @Override
    public boolean puedeIniciarViaje() {
        return false; // ❌ Ya hay un viaje activo
    }

    @Override
    public boolean puedeIrReparacion() {
        return false; // ❌ No puede ir a reparación durante el viaje
    }

    @Override
    public boolean puedeFinalizarViaje() {
        return true;  // ✅ Puede finalizar el viaje
    }

    @Override
    public String getNombre() {
        return "EN_VIAJE";
    }

    @Override
    public String getDescripcion() {
        return "El vehículo está siendo conducido por un usuario";
    }
}
