package Modelo.Managers;

import android.provider.ContactsContract;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import Helpers.DBHelper;
import Modelo.Institution;
import Modelo.Task;
import Modelo.Visit;

public class VisitManager extends Manager<Visit> {

    public VisitManager(DBHelper helper){

        this.dbHelper = helper;
    }


    public List<Visit> simuladorParaTraerVisitasDelEndpoint() {

        List<Visit> lista = new ArrayList<>();

        final Task tareaRAR = new Task(){{id = 1; type = 1;}};
        final Task tareaRGRL = new Task(){{id = 2; type = 2;}};
        final Task tareaCAP = new Task(){{id = 3; type = 3;}};

        final Institution institucion = new Institution()
        {{  id = 1;
            name="Y.P.F.";
            address = "Medrano 950";
            cuit = "30-54668997-9";
            province="Buenos Aires";
            city="C.A.B.A.";
            activity="Extracción de petróleo crudo";
            contract="34343434";
            number="Y.P.F Sucursal 1";
            workers_count=5;
            postal_code="C1015";
            phone="4961-1234";
            contact="Cosme Fulanito";
            email="empresa1@ypf.gob.ar";
            ciiu="01113";
            afip_cod="Código AFIP 1";
        }};
        final Institution institucion2 = new Institution(){{this.id = 2; this.name="Rancho relaxo";}};
        final Institution institucion3 = new Institution(){{this.id = 3; this.name="Santillana";}};
        final Institution institucion4 = new Institution(){{this.id = 4; this.name="MercadoLibre";}};
        final Institution institucion5 = new Institution(){{this.id = 5; this.name="A.F.I.P.";}};
        final Institution institucion6 = new Institution(){{this.id = 6; this.name="Coca-Cola";}};
        final Institution institucion7 = new Institution(){{this.id = 7; this.name="Metrovías";}};
        final Institution institucion8 = new Institution(){{this.id = 8; this.name="Laboratorios Bagó";}};

        Visit visita1 = new Visit(){
            {id = 1;
            institution = institucion;
            status = 1;
            priority = 2;
            tasks.add(tareaRGRL);
            tasks.add(tareaCAP);
            tasks.add(tareaRAR);
            }};
        tareaRGRL.visit = visita1;
        tareaCAP.visit = visita1;
        tareaRAR.visit = visita1;

        /*Visit visita2 = new Visit(){ {this.id = 2; this.institution = institucion2; this.status = 2; this.priority = 1; }};
        Visit visita3 = new Visit(){ {this.id = 3; this.institution = institucion3; this.status = 3; this.priority = 3; }};
        Visit visita4 = new Visit(){ {this.id = 4; this.institution = institucion4; this.status = 3; this.priority = 3; }};
        Visit visita5 = new Visit(){ {this.id = 5; this.institution = institucion5; this.status = 2; this.priority = 3; }};
        Visit visita6 = new Visit(){ {this.id = 6; this.institution = institucion6; this.status = 2; this.priority = 4; }};
        Visit visita7 = new Visit(){ {this.id = 7; this.institution = institucion7; this.status = 1; this.priority = 3; }};
        Visit visita8 = new Visit(){ {this.id = 8; this.institution = institucion8; this.status = 3; this.priority = 5; }};
*/

        lista.add(visita1);
        /*lista.add(visita2);
        lista.add(visita3);
        lista.add(visita4);
        lista.add(visita5);
        lista.add(visita6);
        lista.add(visita7);
        lista.add(visita8);*/

        return  lista;
    }

    /*Este método devolverá las visitas que se encuentren en el dispositivo y que se mostraran
        en la pantalla principal.
    */
    public List<Visit> obtenerVisitasSincronizadas() throws SQLException{
        Dao visitDao = dbHelper.getVisitDao();
        List<Visit> visitas = visitDao.queryForAll();
        return visitas;
    }


    @Override
    public void persist(Visit item) throws SQLException {
        Dao daoVisitas = dbHelper.getVisitDao();

        new InstitutionManager(dbHelper).persist(item.institution);
        new TaskManager(dbHelper).persist(item.tasks);

        if (!daoVisitas.idExists(item.id)) {
            daoVisitas.create(item);
        }
    }

}
