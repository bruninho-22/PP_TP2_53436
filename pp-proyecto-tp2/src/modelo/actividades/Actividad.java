package modelo.actividades;

import excepciones.CupoExcedidoException;
import modelo.Estudiante;
import modelo.Inscripcion;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Actividad implements Serializable {
    private int id;
    private String titulo;
    private int cupoMaximo;

    private List<Inscripcion> inscripciones;

    public static final int CUPO_MINIMO;

    // es un final por eso static, es compartida por todas las instancias
    static {
        CUPO_MINIMO = 1;
        System.out.println("Inicializador estático: se cargó la clase Actividad.");
    }

    // constructor sobrecargado
    public Actividad(int id, String titulo, int cupo) {
        this.id = id;
        this.titulo = titulo;
        this.cupoMaximo = (cupo > CUPO_MINIMO) ? cupo : CUPO_MINIMO; //cuando ingresa un cupo menor al minimo, asigna el cupo minimo
        this.inscripciones = new ArrayList<>();
    }

    public Inscripcion inscribir(Estudiante estudiante) throws CupoExcedidoException {

        //aviso de excepcion cuando se iguala o supera el cupo maximo
       if (this.inscripciones.size() >= this.cupoMaximo){
           throw new CupoExcedidoException("No se puede inscribir a " + estudiante.getNombre() +", el cupo máximo es: " + getCupoMaximo() + " en: " + this.titulo );
       }

        Inscripcion inscripcion = new Inscripcion(this, estudiante, LocalDate.now(), "REGISTRADA");
        inscripciones.add(inscripcion);
        return inscripcion;
    }

    public void mostrarInscripciones() {
        if (inscripciones.isEmpty()) {
            System.out.println("  Sin inscripciones registradas.");
            return;
        }
        System.out.println("   Inscripciones registradas:");
        for (Inscripcion inscripcion : inscripciones) {
            System.out.println("   " + inscripcion.getFecha() +" - "+  inscripcion.getEstado()+ " - " + inscripcion.getEstudiante().getNombre() + " (Legajo: " + inscripcion.getEstudiante().getLegajo() + ")");
        }
    }
    //nuevos metodos
    public final void mostrarIdentificacion() {
        System.out.println("- [" + getTipo() + "] " + titulo + " (ID: " + id + ") - Cupo máx: " + cupoMaximo + " - Costo Mat: $" + calcularCostoMateriales());
    }
    public abstract double calcularCostoMateriales();
    public abstract String getTipo();

    //getters
    public int getId() {
        return id;
    }
    public String getTitulo() {
        return titulo;
    }
    public int getCupoMaximo() {
        return cupoMaximo;
    }
    public List<Inscripcion> getInscripciones() {
        return inscripciones;
    }
}