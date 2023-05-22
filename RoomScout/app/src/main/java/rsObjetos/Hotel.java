package rsObjetos;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class Hotel {
    private String nombre;
    private String direccion;
    private int precio;
    private double latitud;
    private double longitud;
    private int valoracion;
    private String[] favorito_de;

    public Hotel(String nombre, String direccion, int precio, double latitud, double longitud, int valoracion, String[] favorito_de) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.precio = precio;
        this.latitud = latitud;
        this.longitud = longitud;
        this.valoracion = valoracion;
        this.favorito_de = favorito_de;
    }



    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public int getValoracion() {
        return valoracion;
    }

    public void setValoracion(int valoracion) {
        this.valoracion = valoracion;
    }

    public String[] getFavorito_de() {
        return favorito_de;
    }

    public void setFavorito_de(String[] favorito_de) {
        this.favorito_de = favorito_de;
    }
}
