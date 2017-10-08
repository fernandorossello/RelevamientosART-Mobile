package Modelo.Managers;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import Helpers.DBHelper;
import Modelo.CAPResult;
import Modelo.Enums.EnumTareas;
import Modelo.RARResult;
import Modelo.Result;
import Modelo.Task;


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
    }

    public RARResult getRARResult(Task task) throws SQLException {
        Dao daoRARResult = dbHelper.getrARResultDao();
        return (RARResult)daoRARResult.queryBuilder().where().eq("task_id",task.id).queryForFirst();
    }

    public CAPResult getCAPResult(Task task) throws  SQLException {
        Dao daoCAPResult = dbHelper.getCAPResultDao();
        return (CAPResult)daoCAPResult.queryBuilder().where().eq("task_id",task.id).queryForFirst();
    }

    public Result getResult(Task task){
        try {
            if (task.type == EnumTareas.RAR.id) {
                return getRARResult(task);
            }
            if (task.type == EnumTareas.CAPACITACION.id) {
                return  getCAPResult(task);
            }
        } catch (SQLException ex){}

        return null;
    }
}
