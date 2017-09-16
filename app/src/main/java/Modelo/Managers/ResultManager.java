package Modelo.Managers;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import Helpers.DBHelper;
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

            if(!((RARResult)item).workingMen.isEmpty()){
                new WorkingManManager(dbHelper).persist(((RARResult)item).workingMen);
            }
        }

    }

    public RARResult getRARResult(Task task) throws SQLException {
        Dao daoRARResult = dbHelper.getrARResultDao();
        return (RARResult)daoRARResult.queryBuilder().where().eq("task_id",task.id).queryForFirst();
    }

    public Result getResult(Task task){
        try {
            if (task.type == EnumTareas.RAR.id) {
                return getRARResult(task);
            }
        } catch (SQLException ex){}

        return null;
    }
}
