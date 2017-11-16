package Modelo.Managers;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Helpers.DBHelper;
import Modelo.Attendee;
import Modelo.CAPResult;

/**
 * Created by Tomás on 16/9/2017.
 */

public class AttendeeManager extends Manager<Attendee> {
    public AttendeeManager(){}

    public AttendeeManager(DBHelper helper){
        this.dbHelper = helper;
    }

    @Override
    public void persist(Attendee item) throws SQLException {
        Dao daoatt = dbHelper.getAttendeeDao();

        if (!daoatt.idExists(item.id)) {
            daoatt.create(item);
        } else {
            daoatt.update(item);
        }
    }

    public List<Attendee> getAttendees(CAPResult result) throws SQLException {
        List<Attendee> attendees = dbHelper.getAttendeeDao().queryForAll();

        for (Attendee attende: attendees) {
            if (attende.result.id != result.id) {
                attendees.remove(attende);
            }
        }

        return attendees;
    }

    public Attendee getById(int id) throws SQLException {
        return dbHelper.getAttendeeDao().queryForId(id);
    }

    public void delete(Attendee attendee) throws SQLException {
        dbHelper.getAttendeeDao().delete(attendee);
    }

}
