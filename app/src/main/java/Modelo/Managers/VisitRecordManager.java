package Modelo.Managers;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import Helpers.DBHelper;
import Modelo.VisitRecord;

public class VisitRecordManager extends Manager<VisitRecord>{

    public VisitRecordManager(DBHelper helper){
        this.dbHelper = helper;
    }

    @Override
    public void persist(VisitRecord item) throws SQLException {
        Dao daoConstancia = dbHelper.getVisitRecordDao();

        if (!daoConstancia.idExists(item.id)) {
            daoConstancia.create(item);
        } else  {
            daoConstancia.update(item);
        }
    }
}
