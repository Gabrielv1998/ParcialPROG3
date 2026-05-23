package unlar.edu.ar.parcialprog3.models;

public class UsuarioPremium extends Usuario {
    private String metodoPago;

    public UsuarioPremium(String nombre, String metodoPago) {
        super(nombre, null);
        this.metodoPago = metodoPago;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

}
