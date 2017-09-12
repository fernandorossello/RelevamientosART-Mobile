package Modelo.Managers;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import Helpers.DBHelper;
import Modelo.Task;
import Modelo.Visit;

public class TaskManager extends Manager<Task> {

    public TaskManager(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public void persist(Task item) throws SQLException {
        {
            Dao daoTareas = dbHelper.getTaskDao();
            ResultManager  resultadosManager = new ResultManager(dbHelper);

            if (!daoTareas.idExists(item.id)) {
                daoTareas.create(item);
            }


        }
    }

}
