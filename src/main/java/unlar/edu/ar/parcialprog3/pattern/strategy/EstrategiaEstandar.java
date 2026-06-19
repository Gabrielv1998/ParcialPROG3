package unlar.edu.ar.parcialprog3.pattern.strategy;



/**
 * Estrategia Estándar: Costo = minutos * tarifa base.
 */
public class EstrategiaEstandar implements EstrategiaCalculoTarifa {

    @Override
    public Double calcularCosto(Integer minutosTranscurridos, Double tarifaBase) {
        return minutosTranscurridos * tarifaBase;
    }

    @Override
    public String getNombre() {
        return "ESTANDAR";
    }

    @Override
    public String getDescripcion() {
        return "Costo = Minutos × Tarifa Base";
    }
}
