package unlar.edu.ar.parcialprog3.models;

import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
 @Getter 
    @EqualsAndHashCode(of = "id")
    @ToString
public class Usuario {
   
    private String nombre;
     private  final UUID id;

    public Usuario(String nombre, UUID id) {
        this.nombre = nombre;
        this.id = id;
    }

}
