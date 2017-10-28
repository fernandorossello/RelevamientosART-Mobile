package Helpers;


import android.content.Context;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;

import Modelo.Attendee;
import Modelo.CAPResult;
import Modelo.Employee;
import Modelo.Image;
import Modelo.Institution;
import Modelo.Question;
import Modelo.RARResult;
import Modelo.Risk;
import Modelo.Noise;
import Modelo.Task;
import Modelo.Visit;
import Modelo.VisitRecord;
import Modelo.WorkingMan;
import Modelo.Zone;
import Modelo.RGRLResult;

public class DBHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "relevamientos_ormlite.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<Visit, Integer> visitDao;
    private Dao<Institution,Integer> institutionDao;
    private Dao<Task,Integer> taskDao;
    private Dao<VisitRecord,Integer> visitRecordDao;
    private Dao<Image,Integer> imageDao;
    private Dao<RARResult,Integer> rARResultDao;
    private Dao<WorkingMan,Integer> workingManDao;
    private Dao<Risk,Integer> riskDao;
    private Dao<Attendee,Integer> attendeeDao;
    private Dao<Question,Integer> questionDao;
    private Dao<CAPResult,Integer> CAPResultDao;
    private Dao<RGRLResult,Integer> RGLRResultDao;

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
            TableUtils.createTable(connectionSource, Image.class);
            TableUtils.createTable(connectionSource, WorkingMan.class);
            TableUtils.createTable(connectionSource, RARResult.class);
            TableUtils.createTable(connectionSource, CAPResult.class);
            TableUtils.createTable(connectionSource, RGRLResult.class);
            TableUtils.createTable(connectionSource, Risk.class);
            TableUtils.createTable(connectionSource, Noise.class);
            TableUtils.createTable(connectionSource, Attendee.class);
            TableUtils.createTable(connectionSource, Question.class);
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

    public Dao<VisitRecord,Integer> getVisitRecordDao() throws SQLException {
        if (visitRecordDao == null) {
            visitRecordDao= getDao(VisitRecord.class);
        }
        return visitRecordDao;
    }

    public Dao<Image,Integer> getImageDao() throws SQLException {
        if(imageDao == null){
            imageDao = getDao(Image.class);
        }
        return imageDao;
    }

    public Dao<RARResult,Integer> getrARResultDao() throws SQLException{
        if(rARResultDao == null){
            rARResultDao= getDao(RARResult.class);
        }
        return rARResultDao;
    }

    public Dao<WorkingMan,Integer> getworkingManDao() throws SQLException{
        if(workingManDao == null){
            workingManDao= getDao(WorkingMan.class);
        }
        return workingManDao;
    }

    public Dao<Risk,Integer> getRiskDao() throws SQLException{
        if(riskDao == null){
            riskDao = getDao(Risk.class);
        }
        return riskDao;
    }

    public Dao<Attendee,Integer> getAttendeeDao() throws SQLException{
        if(attendeeDao == null){
            attendeeDao = getDao(Attendee.class);
        }
        return attendeeDao;
    }

    public Dao<Question,Integer> getQuestionDao() throws SQLException{
        if (questionDao == null) {
            questionDao = getDao(Question.class);
        }
        return questionDao;
    }

    public Dao<CAPResult,Integer> getCAPResultDao() throws SQLException{
        if(CAPResultDao == null){
            CAPResultDao = getDao(CAPResult.class);
        }
        return CAPResultDao;
    }

    public Dao<RGRLResult,Integer> getRGLRResultDao() throws SQLException{
        if(RGLRResultDao == null){
            RGLRResultDao = getDao(RGRLResult.class);
        }
        return RGLRResultDao;
    }



    @Override
    public void close() {
        super.close();
        institutionDao = null;
        visitDao = null;
    }
}
