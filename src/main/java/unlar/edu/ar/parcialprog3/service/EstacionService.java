package unlar.edu.ar.parcialprog3.service;
import unlar.edu.ar.parcialprog3.models.Vehiculo;

import java.util.ArrayList;

public class EstacionService {

    public ArrayList<Vehiculo> vehiculos;

    public Vehiculo buscarVehiculoPorPatente(String patente) {
        // Lógica para buscar un vehículo por su patente

        try {
            for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getPatente().equals(patente)) {
                return vehiculo;
            }
            }            
        } catch (Exception VehiculoNoEncontradoException ) {
            // TODO: handle exception
        }
        return null;
        
    }

}
