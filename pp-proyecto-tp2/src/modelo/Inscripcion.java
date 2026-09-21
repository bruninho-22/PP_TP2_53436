package modelo;

import modelo.actividades.Actividad;

import java.io.Serializable;
import java.time.LocalDate;

public class Inscripcion implements Serializable {
    private Actividad actividad;
    private Estudiante estudiante;
    private LocalDate fecha;
    private String estado;
    private TicketDeAcceso ticket; // atributo nuevo

    // constructor sobrecargado
    public Inscripcion(Actividad actividad, Estudiante estudiante, LocalDate fecha, String estado) {
        this.actividad = actividad;
        this.estudiante = estudiante;
        this.fecha = fecha;
        this.estado = estado;
        this.ticket = null; // empieza sin ticket
    }

    // getters
    public Actividad getActividad() {
        return actividad;
    }
    public Estudiante getEstudiante() {
        return estudiante;
    }
    public LocalDate getFecha() {
        return fecha;
    }
    public String getEstado() {
        return estado;
    }
    public TicketDeAcceso getTicket() {
        return ticket;
    }

    // cambia el estado de inscripcion
    public void confirmar() {
        this.estado = "CONFIRMADA";
        this.ticket = new TicketDeAcceso(); //cuando se confirma una incripcion, se crea el ticket
    }

    // clase anidada miembro
    public final class TicketDeAcceso implements Serializable {
        private String idTicket;
        private LocalDate fechaEmision;

        // constructor sobrecargado
        public TicketDeAcceso(String idTicket) {
            this.idTicket = idTicket;
            this.fechaEmision = LocalDate.now();
        }

        //getters de TicketDeAcceso
        public String getIdTicket() {
            return idTicket;
        }
        public LocalDate getFechaEmision() {
            return fechaEmision;
        }

        //metodos
        public TicketDeAcceso() {
            this.idTicket = "TICKET-" + actividad.getId() + "-" + estudiante.getLegajo() + "-" + System.currentTimeMillis();
            this.fechaEmision = LocalDate.now();
            System.out.println("Ticket generado para la inscripción: " + idTicket);
        }

        public void enviarTicket() {        //la clase anidada puede ver las variables de la clase madre
            System.out.println("Enviando ticket " + idTicket + " al estudiante " + estudiante.getNombre() + " para la actividad " + actividad.getTitulo());
        }
    }

}
