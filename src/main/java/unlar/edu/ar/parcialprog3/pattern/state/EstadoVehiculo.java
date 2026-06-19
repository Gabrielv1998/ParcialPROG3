package unlar.edu.ar.parcialprog3.pattern.state;

/**
 * Interfaz que define el contrato para los estados del ciclo de vida de un vehículo.
 * Implementa el patrón STATE para evitar estructuras if-else/switch gigantes.
 * 
 * Cada estado conoce qué acciones son permitidas en su contexto y cuáles no.
 */
public interface EstadoVehiculo {
    
    /**
     * Intenta iniciar un viaje con el vehículo
     */
    boolean puedeIniciarViaje();
    
    /**
     * Intenta enviar el vehículo a reparación
     */
    boolean puedeIrReparacion();
    
    /**
     * Intenta finalizar el viaje actual
     */
    boolean puedeFinalizarViaje();
    
    /**
     * Obtiene el nombre del estado
     */
    String getNombre();
    
    /**
     * Obtiene una descripción del estado
     */
    String getDescripcion();
}
    