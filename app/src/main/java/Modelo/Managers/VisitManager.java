package Modelo.Managers;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


import Helpers.DBHelper;
import Modelo.Enums.EnumStatus;
import Modelo.Institution;
import Modelo.Result;
import Modelo.Task;
import Modelo.Visit;

public class VisitManager extends Manager<Visit> {

    public VisitManager(DBHelper helper){

        this.dbHelper = helper;
    }

    public List<Visit> obtenerVisitasSincronizadas(int userId) throws SQLException{
        Dao visitDao = dbHelper.getVisitDao();

        List<Visit> visitas = visitDao.queryBuilder()
                                .where().eq("user_id",userId)
                                .and().not().eq("status",EnumStatus.ENVIADA.id)
                                .query();

        List<Visit> visitas2 = visitDao.queryForAll();

        Collections.sort(visitas);

        return visitas;
    }

    @Override
    public void persist(Visit item) throws SQLException {
        Dao daoVisitas = dbHelper.getVisitDao();

        new InstitutionManager(dbHelper).persist(item.institution);

        for (Task tarea: item.tasks) {
            tarea.visit = item;
        }

        new TaskManager(dbHelper).persist(item.tasks);

        if (!daoVisitas.idExists(item.id)) {
            daoVisitas.create(item);
        } else {
            daoVisitas.update(item);
        }

        if(item.visit_record != null){
            new VisitRecordManager(dbHelper).persist(item.visit_record);
        }
    }

    public List<Visit> obtenerVisitasCompletadas(int userId) throws SQLException{

        List<Visit> visitas = new ArrayList<>();
        for (Visit visita : obtenerVisitasSincronizadas(userId)) {
            if (visita.status == EnumStatus.FINALIZADA.id) {
                visitas.add(visita);
            }
        }

        return visitas;
    }

    public void borrarVisita(Visit visit) throws SQLException {
        Dao visitDao = dbHelper.getVisitDao();
        visitDao.delete(visit);
    }

    public boolean existe(int id) throws SQLException {
        return dbHelper.getVisitDao().idExists(id);
    }

    public void cambiarEstadoVisita(Result resultado) throws SQLException {

        Visit visit = resultado.task.visit;

        if(visit.status == EnumStatus.ASIGNADA.id ) {
                visit.status = EnumStatus.ENPROCESO.id;
        }

        persist(visit);
    }

    public boolean tieneTodosLosResultadosEnviados(Visit visit){
        List<Result> results = new ResultManager(dbHelper).getResults(visit);

        for (Result result : results) {
            if(result == null  || result.getStatus() != EnumStatus.ENVIADA){
                return false;
            }
        }

        return true;
    }
}
