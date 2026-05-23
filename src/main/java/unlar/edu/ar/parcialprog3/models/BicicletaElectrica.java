package unlar.edu.ar.parcialprog3.models;

public class BicicletaElectrica extends Vehiculo {
   private String capacidadCanasto;

    public BicicletaElectrica() {
        super();
    }

    public String getCapacidadCanasto() {
        return capacidadCanasto;
    }

    public void setCapacidadCanasto(String capacidadCanasto) {
        this.capacidadCanasto = capacidadCanasto;
    }

}
