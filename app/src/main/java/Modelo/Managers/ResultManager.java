package Modelo.Managers;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Helpers.DBHelper;
import Modelo.CAPResult;
import Modelo.Enums.EnumTareas;
import Modelo.RARResult;
import Modelo.RGRLResult;
import Modelo.Result;
import Modelo.Task;
import Modelo.Visit;


public class ResultManager extends Manager<Result> {
    public ResultManager(DBHelper dbHelper) {
        super();
        this.dbHelper = dbHelper;
    }

    @Override
    public void persist(Result item) throws SQLException {

        Dao daoResult ;

        if(item.type ==  EnumTareas.RAR.id) {
            daoResult = dbHelper.getrARResultDao();
            if (!daoResult.idExists(item.id)) {
                daoResult.create(item);
            } else {
                daoResult.update(item);
            }

            if(!((RARResult)item).working_men.isEmpty()){
                new WorkingManManager(dbHelper).persist(((RARResult)item).working_men);
            }
        }

        if(item.type == EnumTareas.CAPACITACION.id) {
            daoResult = dbHelper.getCAPResultDao();
            if (!daoResult.idExists(item.id)) {
                daoResult.create(item);
            }else{
                daoResult.update(item);
            }

            if (!((CAPResult)item).attendees.isEmpty()) {
                new AttendeeManager(dbHelper).persist(((CAPResult)item).attendees);
            }
        }

        if(item.type == EnumTareas.RGRL.id) {
            daoResult = dbHelper.getRGLRResultDao();
            if (!daoResult.idExists(item.id)) {
                daoResult.create(item);
            }else{
                daoResult.update(item);
            }

            if (!((RGRLResult)item).questions.isEmpty()) {
                new QuestionManager(dbHelper).persist(((RGRLResult)item).questions);
            }
        }
    }

    public RARResult getRARResult(Task task) throws SQLException {
        Dao daoRARResult = dbHelper.getrARResultDao();
        return (RARResult)daoRARResult.queryBuilder().where().eq("task_id",task.id).queryForFirst();
    }

    public CAPResult getCAPResult(Task task) throws SQLException {
        Dao daoCAPResult = dbHelper.getCAPResultDao();
        return (CAPResult)daoCAPResult.queryBuilder().where().eq("task_id",task.id).queryForFirst();
    }

    public RGRLResult getRGRLResult(Task task) throws SQLException {
        Dao daoRGLRResult = dbHelper.getRGLRResultDao();
        return (RGRLResult)daoRGLRResult.queryBuilder().where().eq("task_id",task.id).queryForFirst();
    }

    public List<Result> getResults(Visit visit){

        List<Result> results = new ArrayList<>();

        for (Task tarea:visit.tasks){
            results.add(getResult(tarea));
        }

        return results;
    }


    public Result getResult(Task task){
        Result result = null;
        try {
            if (task.task_type == EnumTareas.RAR.id) {
                result = getRARResult(task);
            }
            if (task.task_type == EnumTareas.CAPACITACION.id) {
                result = getCAPResult(task);
            }
            if (task.task_type == EnumTareas.RGRL.id) {
                result = getRGRLResult(task);
            }
        } catch (SQLException ex){}

        if(result != null){
            result.task = task;
        }

        return result;
    }
}
