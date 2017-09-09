package Helpers;


import android.content.Context;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import Modelo.Institution;
import Modelo.Task;
import Modelo.User;
import Modelo.Visit;
import Modelo.VisitRecord;
import Modelo.Zone;

public class DBHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "relevamientos_ormlite.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<Visit, Integer> visitDao;
    private Dao<Institution,Integer> institutionDao;
    private Dao<Task,Integer> taskDao;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Visit.class);
            TableUtils.createTable(connectionSource, Institution.class);
            TableUtils.createTable(connectionSource, Task.class);
            TableUtils.createTable(connectionSource, VisitRecord.class);
            TableUtils.createTable(connectionSource, Zone.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    public Dao<Visit, Integer> getVisitDao() throws SQLException {
        if (visitDao == null) {
            visitDao = getDao(Visit.class);
        }
        return visitDao;
    }

    public Dao<Institution, Integer> getInstitutionDao() throws SQLException {
        if (institutionDao == null) {
            institutionDao = getDao(Institution.class);
        }
        return institutionDao;
    }

    public Dao<Task, Integer> getTaskDao() throws SQLException {
        if (taskDao == null) {
            taskDao = getDao(Task.class);
        }
        return taskDao;
    }

    @Override
    public void close() {
        super.close();
        institutionDao = null;
        visitDao = null;
    }
}
