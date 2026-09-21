package modelo;

import java.io.Serializable;

public class Sala implements Serializable {
    private int id;
    private String nombre;

    //constructor sobrecargado
    public Sala(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // getters
    public int getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }

    //setters
    public void setId(int id) {
        this.id = id;
    }
    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return;
        }
        this.nombre = nombre;
    }
}
