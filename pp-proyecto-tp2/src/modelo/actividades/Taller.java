package modelo.actividades;

import modelo.Estudiante;
import modelo.certificacion.Certificable;

public class Taller extends Actividad implements Certificable {
    private boolean requiereNotebook;

    //constructor sobrecargado
    public Taller(int id, String titulo, int cupo, boolean requiereNotebook) {
        super(id, titulo, cupo);
        this.requiereNotebook = requiereNotebook;
    }

    @Override                   //regla de negocio: talleres cuestan $5000 si requieren notebook y $2000 si no
    public double calcularCostoMateriales() {
        return requiereNotebook ? 5000.0 : 2000.0;
    }
    @Override
    public String getTipo() {
        return "Taller (Notebook: " + (requiereNotebook ? "Sí" : "No") + ")";
    }

    @Override
    public String generarCertificado(Estudiante estudiante) {
        return "CERTIFICADO\n" +
                "La entidad " + ENTIDAD_EMISORA + " certifica que el/la estudiante " + estudiante.getNombre() + " (Legajo: " + estudiante.getLegajo() + ")\n" + "ha completado el taller: '" + getTitulo() + "'.";
    }

    //getter
    public boolean isRequiereNotebook() {
        return requiereNotebook;
    }
}
