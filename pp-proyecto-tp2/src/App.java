import excepciones.CupoExcedidoException;
import hilos.EnvioTicketsThread;
import modelo.actividades.Actividad;
import modelo.Estudiante;
import modelo.EventoUniversitario;
import modelo.Sala;
import modelo.actividades.Charla;
import modelo.actividades.Curso;
import modelo.actividades.Taller;
import modelo.certificacion.Certificable;
import modelo.Inscripcion;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String respuesta;

        // carga de estudiantes
        List<Estudiante> estudiantes = new ArrayList<>();
        System.out.println("REGISTRO DE ESTUDIANTES");
        do {
            System.out.print("Ingrese el legajo del estudiante: ");
            String legajo = scanner.nextLine();

            System.out.print("Ingrese el nombre del estudiante: ");
            String nombre = scanner.nextLine();

            estudiantes.add(new Estudiante(legajo, nombre));

            System.out.print("¿Desea ingresar otro estudiante? (s/n): ");
            respuesta = scanner.nextLine();
        } while (respuesta.equalsIgnoreCase("s"));

        int idSala = 1;
        int idActividad = 1;

        // carga de eventos, uno a uno
        do {
            System.out.println("\nINGRESO UN NUEVO EVENTO");
            System.out.print("Ingrese el ID del evento: ");
            String id = scanner.nextLine();

            System.out.print("Ingrese el título del evento: ");
            String titulo = scanner.nextLine();

            System.out.print("Ingrese el costo base: ");
            double costoBase = Double.parseDouble(scanner.nextLine());

            System.out.print("¿Es gratuito? (true/false): ");
            boolean esGratuito = Boolean.parseBoolean(scanner.nextLine());

            // se crea el evento
            EventoUniversitario evento = new EventoUniversitario(id, titulo, costoBase, esGratuito);

            // asignar sala al evento, agregacion
            System.out.print("Ingrese el nombre de la sala asignada: ");
            String nombreSala = scanner.nextLine();
            Sala sala = new Sala(idSala++, nombreSala);
            evento.asignarSala(sala);

            // crear actividades del evento
            String respActividad;
            do {
                System.out.print("\n¿Desea agregar una actividad a este evento? (s/n): "); //es 1--* pero igual pregunto
                respActividad = scanner.nextLine();

                if (respActividad.equalsIgnoreCase("s")) {
                    System.out.print("¿Qué tipo de actividad desea crear? (1: Charla / 2: Taller / 3: Curso): ");
                    String tipoAct = scanner.nextLine();

                    System.out.print("Ingrese el título de la actividad: ");
                    String titAct = scanner.nextLine();

                    System.out.print("Ingrese el cupo máximo: ");
                    int cupoAct = Integer.parseInt(scanner.nextLine());

                    boolean actividadCreada = true; //para validar que sí se elija una opción y no se inscriban estudiantes en la acitivdad anterior usando la bandera actividadCreada
                    //elige entre charla o taller
                    switch (tipoAct) {
                        case "1" -> {
                            System.out.print("Ingrese el nombre del disertante: ");
                            String disertante = scanner.nextLine();
                            evento.crearCharla(idActividad++, titAct, cupoAct, disertante);
                        }
                        case "2" -> {
                            System.out.print("¿Requiere notebook? (true/false): ");
                            boolean reqNotebook = Boolean.parseBoolean(scanner.nextLine());
                            evento.crearTaller(idActividad++, titAct, cupoAct, reqNotebook);
                        }
                        case "3" -> {
                            System.out.print("Ingrese el nivel del curso (1, 2 o 3): ");
                            int nivel = Integer.parseInt(scanner.nextLine());
                            evento.crearCurso(idActividad++, titAct, cupoAct, nivel);
                        }
                        default -> {
                            System.out.println("Opción no válida. No se creó ninguna actividad.");
                            actividadCreada = false;
                        }
                    }

                    // recuperar la actividad recien creada para inscribir alumnos
                    if (actividadCreada) {  //si actividadCreada es false, no entra a este if y como respActividad fue "s" vuelve a preguntar si desea agregar una actividad
                        List<Actividad> acts = evento.getActividades();
                        Actividad actActual = acts.get(acts.size() - 1); //se le resta 1 porque los arreglos empiezan en 0

                        // inscribir estudiantes en la actividad
                        String respInscripcion;
                        do {
                            System.out.print("¿Desea inscribir un estudiante en esta actividad? (s/n): ");
                            respInscripcion = scanner.nextLine();

                            if (respInscripcion.equalsIgnoreCase("s")) {
                                System.out.println("Estudiantes disponibles:");
                                for (int i = 0; i < estudiantes.size(); i++) {
                                    System.out.println((i + 1) + ". " + estudiantes.get(i).getNombre() + " (Legajo: " + estudiantes.get(i).getLegajo() + ")");
                                }

                                System.out.print("Seleccione el número de estudiante: ");
                                int seleccion = Integer.parseInt(scanner.nextLine());

                                if (seleccion >= 1 && seleccion <= estudiantes.size()) {
                                    try {
                                        Inscripcion nuevaInscripcion = actActual.inscribir(estudiantes.get(seleccion - 1)); //guarda la inscripcion del estudiante asignado en nuevaInscripcion
                                        System.out.println("Estudiante inscripto.");

                                        System.out.print("¿Desea confirmar esta inscripción y generar ticket de acceso? (s/n): ");
                                        String confirmar = scanner.nextLine();
                                        if (confirmar.equalsIgnoreCase("s")) {
                                            nuevaInscripcion.confirmar();                   //si el usuario puso s, el estudiante asignado se confirma y se le crea el ticket (con confirmar())
                                        }
                                    } catch (CupoExcedidoException e) {
                                        System.out.println("Error de inscripción: " + e.getMessage());
                                    }
                                } else {
                                    System.out.println("Opción no válida.");
                                }
                            }
                        } while (respInscripcion.equalsIgnoreCase("s"));
                    }
                }
            } while (respActividad.equalsIgnoreCase("s"));

            // inicio del hilo de envio de tickets
            Thread envioTicketsThread = new EnvioTicketsThread(evento);
            envioTicketsThread.start(); // ejecuta run() en un nuevo hilo diferente a este, concurrente, y sigue avanzando el programa

            System.out.println("\nDATOS DEL EVENTO ORIGINAL");
            evento.mostrarDatos();


            System.out.println("REPORTE DE ACTIVIDADES POR TIPO");
            // listas fuertemente tipadas
            List<Charla> charlas = evento.filtrarActividadesPorTipo(Charla.class);
            List<Taller> talleres = evento.filtrarActividadesPorTipo(Taller.class);
            List<Curso> cursos = evento.filtrarActividadesPorTipo(Curso.class);

            // mostrar antidad de actividades de cada tipo
            System.out.println("Cantidad de Charlas: " + charlas.size());
            System.out.println("Cantidad de Talleres: " + talleres.size());
            System.out.println("Cantidad de Cursos: " + cursos.size());

            // costo de materiales correspondiente a cada tipo
            System.out.println("\nCosto de materiales por tipo de actividad:");
            System.out.println("- Charlas:  $" + evento.calcularCostoMateriales(charlas));
            System.out.println("- Talleres: $" + evento.calcularCostoMateriales(talleres));
            System.out.println("- Cursos:   $" + evento.calcularCostoMateriales(cursos));

            System.out.println("- Total acumulado (todas las actividades): $" + evento.calcularCostoMateriales(evento.getActividades()));

            // emision de certificados
            System.out.println("\nEMISIÓN DE CERTIFICADOS");
            if (evento.getActividades().isEmpty()) {
                System.out.println("No hay actividades asignadas al evento.");
            } else {
                for (Actividad act : evento.getActividades()) {
                    // verificar si la actividad implementa la interfaz Certificable
                    if (act instanceof Certificable certificable) {
                        System.out.println("\nActividad con certificación: " + act.getTitulo());

                        if (act.getInscripciones().isEmpty()) {
                            System.out.println("No hay estudiantes inscriptos en esta actividad.");
                        } else {
                            for (Inscripcion insc : act.getInscripciones()) {
                                // invocar el metodo polimórfico a través de la interfaz
                                String certificado = certificable.generarCertificado(insc.getEstudiante());
                                System.out.println("----------------------------------------");
                                System.out.println(certificado);
                            }
                        }
                    } else {
                        System.out.println("\nLa actividad '" + act.getTitulo() + "' (" + act.getTipo() + ") NO emite certificados.");
                    }
                }
            }

            // se crea la copia (no copia las actividades)
            EventoUniversitario copiaEvento = new EventoUniversitario(evento); //solo actividad es abstacta, si se puede usar new

            System.out.println("\nDATOS DE LA COPIA");
            copiaEvento.mostrarDatos();

            System.out.println("\nPERSISTENCIA Y RECUPERACIÓN DEL ARCHIVO");
            try{
                //guardar evento
                boolean guardado = evento.persistirEvento();
                if(guardado){
                    System.out.println("Evento persistido existosamente en el disco.");
                }

                //leer evento guardado (deserializacion)
                EventoUniversitario eventoGuardado = evento.recuperarEvento(evento.getId());
                System.out.println("Evento recuperado existosamente desde el archivo binario.");
                eventoGuardado.mostrarDatos();

            } catch (FileNotFoundException e) {
                System.out.println("No se encontró el archivo del evento: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                System.out.println("No se encontró la clase del archivo: "+ e.getMessage());
            } catch (IOException e){
                System.out.println("Error de entrada/salida: "+ e.getMessage());
            } finally {
                System.out.println("Proceso de persistencia/lectura finalizado para el evento: "+ evento.getId());
            }

            // esperar que termine el hilo secundario antes de solicitar el próximo evento
            try {
                envioTicketsThread.join(); //espera a que muera el hilo
            } catch (InterruptedException e) { //lanza una excepcion si otro hilo interrumpe al actual
                Thread.currentThread().interrupt();
            }

            System.out.print("\n¿Desea registrar otro evento? (s/n): ");
            respuesta = scanner.nextLine();

        } while (respuesta.equalsIgnoreCase("s"));

        System.out.println("\n---------------------------------");
        System.out.println("Totalidad de eventos creados: " + EventoUniversitario.getCantidadEventos());

        scanner.close(); //buena práctica para liberar recursos
    }
}