package unlar.edu.ar.parcialprog3.models;

import lombok.Getter;
import lombok.Setter;

public class Vehiculo {
    @Getter @Setter private String patente;
    @Getter @Setter private int porcentajeBateria;
    @Getter @Setter private double tarifaBase;

    public Vehiculo() {
    }


    
}
