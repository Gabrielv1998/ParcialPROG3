package unlar.edu.ar.parcialprog3.pattern.strategy;



/**
 * Estrategia Hora Pico: Agrega 40% al costo final.
 * Incentiva rotación en momentos de congestión urbana.
 */
public class EstrategiaHoraPico implements EstrategiaCalculoTarifa {

    private static final Double RECARGO_PICO = 0.40; // 40%

    @Override
    public Double calcularCosto(Integer minutosTranscurridos, Double tarifaBase) {
        Double costoBase = minutosTranscurridos * tarifaBase;
        return costoBase * (1 + RECARGO_PICO); // +40% al total
    }

    @Override
    public String getNombre() {
        return "HORA_PICO";
    }

    @Override
    public String getDescripcion() {
        return "Costo = (Minutos × Tarifa Base) × 1.40 (40% extra en congestión)";
    }
}
