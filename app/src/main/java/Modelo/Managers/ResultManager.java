package Modelo.Managers;

import java.sql.SQLException;

import Helpers.DBHelper;
import Modelo.Result;


public class ResultManager extends Manager<Result> {
    public ResultManager(DBHelper dbHelper) {
        super();
        this.dbHelper = dbHelper;
    }

    @Override
    public void persist(Result item) throws SQLException {
        

    }
}
