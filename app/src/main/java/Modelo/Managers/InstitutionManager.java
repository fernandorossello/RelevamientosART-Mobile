package Modelo.Managers;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import Helpers.DBHelper;
import Modelo.Institution;


public class InstitutionManager extends Manager<Institution>{

    public InstitutionManager(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }


    @Override
    public void persist(Institution item) throws SQLException {
            Dao daoInstitucion = dbHelper.getInstitutionDao();

            if (!daoInstitucion.idExists(item.id)) {
                daoInstitucion.create(item);
            }
    }
}
