package modelo;

import java.io.Serializable;

public class Estudiante implements Serializable {
    private String legajo;
    private String nombre;

    // constructor sobrecargado
    public Estudiante(String legajo, String nombre){
        this.legajo = legajo;
        this.nombre = nombre;
    }

    //getters
    public String getLegajo() {
        return legajo;
    }
    public String getNombre() {
        return nombre;
    }
}