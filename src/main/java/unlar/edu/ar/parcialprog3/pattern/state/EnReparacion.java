package unlar.edu.ar.parcialprog3.pattern.state;



/**
 * Estado: EN REPARACIÓN
 * El vehículo tiene fallas y no puede ser usado.
 */
public class EnReparacion implements EstadoVehiculo {

    @Override
    public boolean puedeIniciarViaje() {
        return false; // ❌ No puede usarse si está roto
    }

    @Override
    public boolean puedeIrReparacion() {
        return false; // ❌ Ya está en reparación
    }

    @Override
    public boolean puedeFinalizarViaje() {
        return false; // ❌ No hay viaje activo
    }

    @Override
    public String getNombre() {
        return "EN_REPARACION";
    }

    @Override
    public String getDescripcion() {
        return "El vehículo tiene fallas mecánicas o de batería y requiere mantenimiento";
    }
}
