package modelo;

import modelo.actividades.Actividad;
import modelo.actividades.Charla;
import modelo.actividades.Curso;
import modelo.actividades.Taller;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class EventoUniversitario implements Serializable {
    private final String Id;
    private String titulo;
    private double costoBase;
    private boolean gratuito;

    private Sala sala;                   //                 agregacion
    private List<Actividad> actividades; //                 composicion

    private static int cantidadEventos;
    static {
        cantidadEventos = 0;
        System.out.println("Inicializador estático: se cargó la clase EventoUniversitario.");
    }

    // Constructor principal sobrecargado
    public EventoUniversitario(String id, String nombre, double costo, boolean esGratuito) {
        this.Id = id;
        setTitulo(nombre); // Se usa setTitulo para aplicar la validación
        this.gratuito = esGratuito;
        this.costoBase = gratuito ? 0 : costo;

        // inicializar la lista de composición (esta adentro de la clase)
        this.actividades = new ArrayList<>();

        cantidadEventos++;
    }

    // Constructor de copia
    public EventoUniversitario(EventoUniversitario otroEvento) {
        this(                                                   //con el this se le asigna una sala a la nueva instacnia
                otroEvento.Id + "-COPIA",
                otroEvento.titulo,
                otroEvento.costoBase,
                otroEvento.gratuito
        );
    }

    // asigna la sala agregacion (afuera del objeto))
    public void asignarSala(Sala sala) {
        this.sala = sala;
    }

    //crean las instancias de charla y taller
    public void crearCharla(int id, String titulo, int cupo, String disertante) {
        this.actividades.add(new Charla(id, titulo, cupo, disertante));
    }
    public void crearTaller(int id, String titulo, int cupo, boolean requiereNotebook) {
        this.actividades.add(new Taller(id, titulo, cupo, requiereNotebook));
    }
    public void crearCurso(int id, String titulo, int cupo, int nivel) {
        this.actividades.add(new Curso(id, titulo, cupo, nivel));
    }

    public void crearActividad(String tipo, int id, String titulo, int cupo, String extra) {
        if (tipo.equalsIgnoreCase("Charla")) {
            this.crearCharla(id, titulo, cupo, extra);
        } else if (tipo.equalsIgnoreCase("Taller")) {
            boolean requiereNotebook = Boolean.parseBoolean(extra);
            this.crearTaller(id, titulo, cupo, requiereNotebook);
        } else if (tipo.equalsIgnoreCase("Curso")) {
            int nivel = Integer.parseInt(extra);
            this.crearCurso(id, titulo, cupo, nivel);
        }
    }

    // acceder a las actividades para poder inscribir estudiantes
    public List<Actividad> getActividades() {
        return Collections.unmodifiableList(actividades);
    }

    // Setter con validación correcta
    public void setTitulo(String nombre) {
        if (nombre != null && !nombre.isBlank()) {
            this.titulo = nombre;
        }
    }

    // Getters
    public String getId() {
        return Id;
    }
    public String getTitulo() {
        return titulo;
    }
    public double getCostoBase() {
        return costoBase;
    }
    public boolean esGratuito() {
        return gratuito;
    }
    public static int getCantidadEventos() {
        return cantidadEventos;
    }
    public Sala getSala() {
        return sala;
    }

    // nuevo calculo del costo estimado
    public double calcularCostoEstimado() {
        if (gratuito) {
            return 0.0;
        }
        double sumaMateriales = 0.0;
        for (Actividad act : actividades) {
            sumaMateriales += act.calcularCostoMateriales(); // polimorfismo
        }
        return (costoBase + sumaMateriales) * 1.21;
    }



    // muestra datos
    public void mostrarDatos() {
        System.out.println("----------------------------------------");
        System.out.println("Evento ID: " + Id);
        System.out.println("Título: " + titulo);
        System.out.println("Gratuito: " + (gratuito ? "Sí" : "No"));
        System.out.println("Costo Estimado: $" + calcularCostoEstimado());
        System.out.println("Sala: " + (sala != null ? sala.getNombre() + " (ID: " + sala.getId() + ")" : "Sin sala asignada"));
        System.out.println("Actividades:");
        if (actividades.isEmpty()) {
            System.out.println("Sin actividades registradas.");
        } else {
            for (Actividad act : actividades) {
                act.mostrarIdentificacion(); // metodo final polimórfico
                act.mostrarInscripciones();
            }
        }
    }

    public boolean persistirEvento() throws IOException {       //guarda el objeto actual en un archivo
        String nombreArchivo = "evento_" + this.Id + ".dat";
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(
                             new FileOutputStream(nombreArchivo))) {

            oos.writeObject(this);
            return true;
        }
    }

    public EventoUniversitario recuperarEvento(String id)  throws IOException, ClassNotFoundException {     //carga un objeto leyendo un archivo

        String nombreArchivo = "evento_" + id + ".dat";

        try (ObjectInputStream ois =  //Se usa un patron try-with-resources para devolver el objeto recuperado
                     new ObjectInputStream(
                             new FileInputStream(nombreArchivo))) {

            return (EventoUniversitario) ois.readObject();
        }
    }

    //metodo para filtrar actividades
    public <T extends Actividad> List<T> filtrarActividadesPorTipo(Class<T> tipo) {
        List<T> filtradas = new ArrayList<>();
        for (Actividad act : this.actividades) {
            if (tipo.isInstance(act)) {           // isInstance evalua en tiempo de ejecución si el objeto pertenece a la clase T
                filtradas.add(tipo.cast(act));
            }
        }
        return filtradas;
    }

    public double calcularCostoMateriales(List<? extends Actividad> listaActividades) {
        double total = 0.0;
        if (listaActividades != null) {
            for (Actividad act : listaActividades) {
                total += act.calcularCostoMateriales();
            }
        }
        return total;
    }

}
