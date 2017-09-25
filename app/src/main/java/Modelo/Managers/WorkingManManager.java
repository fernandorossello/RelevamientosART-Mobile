package Modelo.Managers;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

        if(!item.riskList.isEmpty()) {
            new RiskManager(dbHelper).persist(item.riskList);
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
}
