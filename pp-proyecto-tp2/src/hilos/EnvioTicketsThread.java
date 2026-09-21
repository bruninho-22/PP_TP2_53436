package hilos;

import modelo.EventoUniversitario;
import modelo.Inscripcion;
import modelo.actividades.Actividad;

public class EnvioTicketsThread extends Thread{
    private EventoUniversitario evento;

    //constructor sobrecargado
    public EnvioTicketsThread(EventoUniversitario evento) {
        super("Hilo-Envio-Tickets");
        this.evento = evento;
    }

    //metodos
    @Override
    public void run() {
        System.out.println("[" + getName() + "] Inicio del envío de tickets.");
        for (Actividad actividad : evento.getActividades()) {
            for (Inscripcion inscripcion : actividad.getInscripciones()) {
                if ("CONFIRMADA".equals(inscripcion.getEstado()) && inscripcion.getTicket() != null) { //ticket null no ejecuta el if
                    inscripcion.getTicket().enviarTicket();
                    try {
                        Thread.sleep(500); // simula un comportamiento no determinista de los hilos, envia su resultado en cualquier momento durante la ejecucion de main
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        System.out.println("[" + getName() + "] Fin del envío de tickets.");
    }
}
