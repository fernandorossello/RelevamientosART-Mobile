package Modelo.Managers;

import java.util.ArrayList;
import java.util.List;


import Modelo.Institution;
import Modelo.Visit;

public class VisitManager {

    /*Este método devolverá las visitas que se encuentren en el dispositivo y que se mostraran
        en la pantalla principal.
    */
    public List<Visit> obtenerVisitasSincronizadas() {

        List<Visit> lista = new ArrayList<>();

        final Institution institucion = new Institution(){{ this.name="Thomas Bundle Institution of Programming";}};

        Visit visita1 = new Visit(){ {this.id = 1; this.institution = institucion;  }};
        Visit visita2 = new Visit(){ {this.id = 2; this.institution = institucion;  }};;
        Visit visita3 = new Visit(){ {this.id = 3; this.institution = institucion;  }};;

        lista.add(visita1);
        lista.add(visita2);
        lista.add(visita3);

        return  lista;
    }
}
