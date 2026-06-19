package unlar.edu.ar.parcialprog3.pattern.strategy;



import lombok.Data;

/**
 * CalculadoraTarifaAdaptativa: Contexto del patrón Strategy.
 * Permite cambiar la estrategia en tiempo de ejecución sin reiniciar la aplicación.
 */
@Data
public class CalculadoraTarifaAdaptativa {

    private EstrategiaCalculoTarifa estrategiaActiva;

    public CalculadoraTarifaAdaptativa() {
        this.estrategiaActiva = new EstrategiaEstandar();
    }

    public CalculadoraTarifaAdaptativa(EstrategiaCalculoTarifa estrategiaActiva) {
        this.estrategiaActiva = estrategiaActiva;
    }

    /**
     * Calcula el costo usando la estrategia activa actual.
     */
    public Double calcularCosto(Integer minutosTranscurridos, Double tarifaBase) {
        if (estrategiaActiva == null) {
            estrategiaActiva = new EstrategiaEstandar();
        }
        return estrategiaActiva.calcularCosto(minutosTranscurridos, tarifaBase);
    }

    /**
     * Cambia la estrategia dinámicamente durante la ejecución.
     */
    public void cambiarEstrategia(EstrategiaCalculoTarifa nuevaEstrategia) {
        this.estrategiaActiva = nuevaEstrategia;
    }

    /**
     * Obtiene información de la estrategia activa.
     */
    public String obtenerInfoEstrategia() {
        return estrategiaActiva.getNombre() + ": " + estrategiaActiva.getDescripcion();
    }
}
