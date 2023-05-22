package rsObjetos;

public class Reserva {
    private String usuario;
    private String hotel;
    private String fechaIda;
    private String fechaVuelta;
    private int personas;
    private String precio;

    public Reserva(String usuario, String hotel, String fechaIda, String fechaVuelta, int personas,String precio) {
        this.usuario = usuario;
        this.hotel = hotel;
        this.fechaIda = fechaIda;
        this.fechaVuelta = fechaVuelta;
        this.personas = personas;
        this.precio = precio;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public String getFechaIda() {
        return fechaIda;
    }

    public void setFechaIda(String fechaIda) {
        this.fechaIda = fechaIda;
    }

    public String getFechaVuelta() {
        return fechaVuelta;
    }

    public void setFechaVuelta(String fechaVuelta) {
        this.fechaVuelta = fechaVuelta;
    }

    public int getPersonas() {
        return personas;
    }

    public void setPersonas(int personas) {
        this.personas = personas;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
}
