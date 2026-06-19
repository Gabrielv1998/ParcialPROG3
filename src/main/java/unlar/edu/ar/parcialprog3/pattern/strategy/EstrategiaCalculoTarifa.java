package unlar.edu.ar.parcialprog3.pattern.strategy;


/**
 * Interfaz que define la estrategia de cálculo de tarifas.
 * Permite cambiar dinámicamente cómo se calcula el costo del viaje
 * sin reiniciar la aplicación (patrón STRATEGY).
 */
public interface EstrategiaCalculoTarifa {

    /**
     * Calcula el costo total del viaje basado en los parámetros.
     *
     * @param minutosTranscurridos Tiempo del viaje en minutos
     * @param tarifaBase Costo base por minuto
     * @return Monto total a cobrar
     */
    Double calcularCosto(Integer minutosTranscurridos, Double tarifaBase);

    /**
     * Obtiene el nombre de la estrategia.
     */
    String getNombre();

    /**
     * Obtiene una descripción de cómo se calcula.
     */
    String getDescripcion();
}
