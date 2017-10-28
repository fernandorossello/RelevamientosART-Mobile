package Modelo.Managers;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import Helpers.DBHelper;
import Modelo.RARResult;
import Modelo.WorkingMan;

public class WorkingManManager extends Manager<WorkingMan> {

    public WorkingManManager(DBHelper helper){
        this.dbHelper = helper;
    }

    @Override
    public void persist(WorkingMan item) throws SQLException {
        Dao daowm = dbHelper.getworkingManDao();

        if (!daowm.idExists(item.id)) {
            daowm.create(item);
        } else {
            daowm.update(item);
        }

        if(!item.risk_list.isEmpty()) {
            new RiskManager(dbHelper).persist(item.risk_list);
        }
    }

    public List<WorkingMan> getWorkingMen(RARResult result) throws  SQLException {
        List<WorkingMan> trabajadores = dbHelper.getworkingManDao().queryForAll();

        for (WorkingMan trabajador: trabajadores) {
            if(trabajador.result.id != result.id){
                trabajadores.remove(trabajador);
            }
        }

        return trabajadores;
    }

    public WorkingMan getById(int id) throws SQLException {
        return dbHelper.getworkingManDao().queryForId(id);
    }

    public void delete(WorkingMan workingMan) throws SQLException {
        dbHelper.getworkingManDao().delete(workingMan);
    }

}
