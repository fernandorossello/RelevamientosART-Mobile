package Modelo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@DatabaseTable
public class Visit implements Serializable {

    @DatabaseField(id = true)
    public int id;

    @DatabaseField(foreign = true,foreignAutoRefresh = true)
    public Institution institution;

    @DatabaseField
    public int priority;

    //TODO: Ver como se marca relación muchos a muchos
    public List<Task> tasks;

    public VisitRecord visitRecord;

    @DatabaseField
    public int status;

    @DatabaseField
    public Date postponed_at;

    public String nombreInstitucion(){
        return this.institution.name;
    }

}
