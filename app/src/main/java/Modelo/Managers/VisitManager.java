package Modelo.Managers;

import android.widget.Toast;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import Helpers.DBHelper;
import Modelo.Institution;
import Modelo.Visit;

public class VisitManager extends Manager<Visit> {

    public VisitManager(DBHelper helper){

        this.dbHelper = helper;
    }


    public List<Visit> simuladorParaTraerVisitasDelEndpoint() {

        List<Visit> lista = new ArrayList<>();

        final Institution institucion = new Institution(){{this.id = 1; this.name="Thomas Bundle Institution of Programming";}};
        final Institution institucion2 = new Institution(){{this.id = 2; this.name="Institución de prueba";}};

        Visit visita1 = new Visit(){ {this.id = 1; this.institution = institucion; this.status = 1; this.priority = 2; }};
        Visit visita2 = new Visit(){ {this.id = 2; this.institution = institucion; this.status = 2; this.priority = 1; }};
        Visit visita3 = new Visit(){ {this.id = 3; this.institution = institucion; this.status = 3; this.priority = 3; }};
        Visit visita4 = new Visit(){ {this.id = 4; this.institution = institucion2; this.status = 3; this.priority = 3; }};
        Visit visita5 = new Visit(){ {this.id = 5; this.institution = institucion2; this.status = 3; this.priority = 3; }};
        Visit visita6 = new Visit(){ {this.id = 6; this.institution = institucion2; this.status = 3; this.priority = 4; }};
        Visit visita7 = new Visit(){ {this.id = 7; this.institution = institucion2; this.status = 3; this.priority = 3; }};
        Visit visita8 = new Visit(){ {this.id = 8; this.institution = institucion2; this.status = 3; this.priority = 5; }};
        Visit visita9 = new Visit(){ {this.id = 9; this.institution = institucion2; this.status = 3; this.priority = 3; }};
        Visit visita10 = new Visit(){ {this.id = 10; this.institution = institucion2; this.status = 3; this.priority = 3; }};


        lista.add(visita1);
        lista.add(visita2);
        lista.add(visita3);
        lista.add(visita4);
        lista.add(visita5);
        lista.add(visita6);
        lista.add(visita7);
        lista.add(visita8);
        lista.add(visita9);
        lista.add(visita10);


        return  lista;
    }

    /*Este método devolverá las visitas que se encuentren en el dispositivo y que se mostraran
        en la pantalla principal.
    */
    public List<Visit> obtenerVisitasSincronizadas() throws SQLException{
        Dao visitDao = dbHelper.getVisitDao();
        Dao institutionDao = dbHelper.getInstitutionDao();
        List<Visit> visitas = visitDao.queryForAll();
        return visitas;
    }


    @Override
    public void persist(Visit item) throws SQLException {
        Dao daoVisitas = dbHelper.getVisitDao();

        new InstitutionManager(dbHelper).persist(item.institution);

        if (!daoVisitas.idExists(item.id)) {
            daoVisitas.create(item);
        }
    }

}
