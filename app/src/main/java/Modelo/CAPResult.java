package Modelo;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import Modelo.Enums.EnumTareas;

@DatabaseTable
public class CAPResult extends Result implements Serializable{

    public CAPResult(){
        if(attendees == null) {
            attendees = new ArrayList<>();
        }

        type = EnumTareas.CAPACITACION.id;
    }

    @DatabaseField
    public int attendees_count;

    @Expose
    @ForeignCollectionField
    public Collection<Attendee> attendees;

    @Expose
    @DatabaseField
    public String courseName;

    @Expose
    @DatabaseField
    public String contents;

    @Expose
    @DatabaseField
    public String methodology;

}
