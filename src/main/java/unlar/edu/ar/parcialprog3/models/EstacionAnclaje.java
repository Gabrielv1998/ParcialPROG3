package unlar.edu.ar.parcialprog3.models;
import unlar.edu.ar.parcialprog3.models.Vehiculo;
import java.util.ArrayList;
import java.util.List;
public class EstacionAnclaje {

    private String nombre;
    private List<Vehiculo> vehiculos;

    public EstacionAnclaje() {
        this.vehiculos = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(List<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }

}
