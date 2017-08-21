package Modelo.Managers;


import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import Helpers.DBHelper;


public abstract class Manager<T> {

    protected DBHelper dbHelper;

    public abstract void persist(T item) throws SQLException;

    public void persist(List<T> items)throws SQLException{
        for (T item:items) {
            persist(item);
        }
    };

}
