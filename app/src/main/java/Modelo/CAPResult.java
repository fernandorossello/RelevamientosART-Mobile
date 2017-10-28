package Modelo;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import Modelo.Enums.EnumStatus;
import Modelo.Enums.EnumTareas;

@DatabaseTable
public class CAPResult extends Result implements Serializable{

    public CAPResult(){
        if(attendees == null) {
            attendees = new ArrayList<>();
        }

        type = EnumTareas.CAPACITACION.id;
    }


    @Expose
    @ForeignCollectionField
    public Collection<Attendee> attendees;

    @Expose
    @DatabaseField
    public String course_name;

    @Expose
    @DatabaseField
    public String contents;

    @Expose
    @DatabaseField
    public String methodology;


    @Override
    public EnumStatus getStatus() {

        if(attendees.isEmpty() && methodology.trim().isEmpty() && course_name.trim().isEmpty() && contents.trim().isEmpty()){
            return EnumStatus.PENDIENTE;
        } else if(!attendees.isEmpty() && !methodology.trim().isEmpty() && !course_name.trim().isEmpty() && !contents.trim().isEmpty()){
            return EnumStatus.FINALIZADA;
        }
        return EnumStatus.ENPROCESO;
    }

}
