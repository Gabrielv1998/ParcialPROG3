package unlar.edu.ar.parcialprog3.pattern.strategy;

/**
 * Estrategia Temporal Climático: Suma $150 de seguro.
 * Cubre gastos de seguros especiales por lluvia o tormenta.
 */
public class EstrategiaTemporalClimatico implements EstrategiaCalculoTarifa {

    private static final Double RECARGO_SEGURO = 150.0; // $150 planos

    @Override
    public Double calcularCosto(Integer minutosTranscurridos, Double tarifaBase) {
        Double costoBase = minutosTranscurridos * tarifaBase;
        return costoBase + RECARGO_SEGURO; // Suma plana de $150
    }

    @Override
    public String getNombre() {
        return "TEMPORAL_CLIMATICO";
    }

    @Override
    public String getDescripcion() {
        return "Costo = (Minutos × Tarifa Base) + $150 (seguro clima)";
    }
}
