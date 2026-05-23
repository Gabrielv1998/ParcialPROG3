package unlar.edu.ar.parcialprog3.models;

public class Monopatin  extends Vehiculo {
    private boolean tieneAmortiguacion;     

    public Monopatin() {
        super();
    }

    public boolean isTieneAmortiguacion() {
        return tieneAmortiguacion;
    }

    public void setTieneAmortiguacion(boolean tieneAmortiguacion) {
        this.tieneAmortiguacion = tieneAmortiguacion;
    }

}
