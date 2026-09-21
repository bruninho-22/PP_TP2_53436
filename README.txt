Trabajo Practico 2 Programación Orientada a Objetos en Java - 53436

Sistema que gestiona eventos, salas, actividades (charlas, talleres y cursos), estudiantes, inscripciones, certificados y tickets.

Funcionalidades:
-EventoUniversitario administra el ciclo de vida de sus actividades.
-Sala se le asigna al evento pero vive de forma independiente.
-Charla, Taller y Curso son clases heredadas de la clase abstracta Actividad.
-Inscripción es una relación entre Actividad y Estudiante, registra fecha y estado.
-Paquetes modelo, actividades, excepciones, certificación, hilos para una mejor organización del programa.
-Taller y Curso implementan la interface Certificable.
-Uso de Try-catch-finally para capturar excepciones.
-Permite persistir, mediante serialización de objetos de los eventos creados.
-Envío de certificados a los estudiantes que participan en actividades que implementan Certificable.
-Filtro de actividades de cada evento por tipo de actividad con el uso de generics y wilcards.
-Muestra de costo de materiales por cada tipo de actividad.
-TicketDeAcceso clase anidada dentro de Inscripción.
-Se generan y envían tickets desde un hilo concurrente para los alumnos inscriptos en actividades.

Instrucciones:
1. Abrir el proyecto en Intellij IDEA.
2. Seleccionar la opción "Run 'App'".
3. Responder lo solicitado por la terminal.
4. Visualizar los resultados generados por el programa en la terminal.