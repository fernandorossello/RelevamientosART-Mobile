package Modelo.Managers;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Modelo.Attendee;

/**
 * Created by Tomás on 16/9/2017.
 */

public class AttendeeManager extends Manager<Attendee> {
    @Override
    public void persist(Attendee item) throws SQLException {
    }

    public List<Attendee> attendeesEjemplo(){
        List<Attendee> attendees = new ArrayList<>();

        attendees.add(new Attendee(){
            {
                name = "Fernando";
                lastName = "Roselló";
                cuil = "34898398";
            }
        });

        attendees.add(new Attendee(){
            {
                name = "Tomás";
                lastName = "Ramos";
                cuil = "30989490";}
        });

        attendees.add(new Attendee(){
            {
                name = "Julieta";
                lastName = "Feld";
                cuil = "31552899";}
        });

        return attendees;
    }


}
