package Modelo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    @ForeignCollectionField
    public Collection<Attendee> attendees;

    @DatabaseField
    public String courseName;

    @DatabaseField
    public String contents;

    @DatabaseField
    public String methodology;
}
