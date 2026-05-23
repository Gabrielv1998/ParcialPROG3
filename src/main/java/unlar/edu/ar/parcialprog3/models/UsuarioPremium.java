package unlar.edu.ar.parcialprog3.models;

public class UsuarioPremium extends Usuario {
    private String metodoPago;

    public UsuarioPremium() {
        super();
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

}
