package modelo.certificacion;

import modelo.Estudiante;

public interface Certificable {
      String ENTIDAD_EMISORA = "UTN - FRM";      //las variables en interfaces son CONSTANTES (pulic static final)


     //metodo asbtracto
     String generarCertificado(Estudiante estudiante);
}

