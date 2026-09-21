package modelo.actividades;

import modelo.Estudiante;
import modelo.certificacion.Certificable;

public class Curso extends Actividad implements Certificable {
    private int nivel;

    //constructor sobrecargado
    public Curso(int id, String titulo, int cupoMaximo, int nivel) {
        super(id, titulo, cupoMaximo);
        this.nivel = nivel;
    }

    //getters
    public int getNivel(){
        return nivel;
    }

    //métodos heredados
    @Override
    public double calcularCostoMateriales(){        //regla de negocio
        if (1 <= nivel && nivel <= 3)
            return nivel * 1000;
        else
            return 0;

    }
    @Override
    public String getTipo(){
        return "Curso (Nivel: " + nivel + ")";
    }


    @Override
    public String generarCertificado(Estudiante estudiante) {
        return "CERTIFICADO\n" +
                "La entidad " + ENTIDAD_EMISORA + " certifica que el/la estudiante " + estudiante.getNombre() + " (Legajo: " + estudiante.getLegajo() + ")\n" + "ha completado el Curso: '" + getTitulo() + "'.";
    }
}
