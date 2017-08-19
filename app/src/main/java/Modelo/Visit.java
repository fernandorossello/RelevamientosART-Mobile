package Modelo;

import java.util.Date;
import java.util.List;

public class Visit {

    public int id;

    public User user;

    public Institution institution;

    public int priority;

    public List<Task> tasks;

    public VisitRecord visitRecord;

    public int status;

    public Date postponed_at;

    public String nombreInstitucion(){
        return this.institution.name;
    }

}
