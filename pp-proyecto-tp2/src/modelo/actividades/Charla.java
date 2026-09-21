package modelo.actividades;

public class Charla extends Actividad {
    private String disertante;

    //constructor sobrecargado
    public Charla(int id, String titulo, int cupo, String disertante){
        super(id, titulo, cupo);    //invoca al constructor de la superclase
        this.disertante = disertante;
    }

    @Override           //regla de negocio: las charlas son gratuitas (sobreescribe el metodo heredado)
    public double calcularCostoMateriales() {
        return 0.0;
    }
    @Override
    public String getTipo() {
        return "Charla (Disertante: " + disertante + ")";
    }

    //getter
    public String getDisertante() {
        return disertante;
    }
}

