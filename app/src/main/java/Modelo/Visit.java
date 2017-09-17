package Modelo;

import com.j256.ormlite.dao.EagerForeignCollection;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.dao.LazyForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import Modelo.Enums.EnumTareas;

@DatabaseTable
public class Visit implements Serializable {

    public Visit(){
        tasks = new ArrayList<>();
    }

    @DatabaseField(id = true)
    public int id;

    @DatabaseField(foreign = true,foreignAutoRefresh = true)
    public Institution institution;

    @DatabaseField
    public int type;

    @DatabaseField
    public int priority;

    @ForeignCollectionField(eager = true)
    public Collection<Task> tasks;

    public VisitRecord visitRecord;

    @DatabaseField
    public int status;

    @DatabaseField
    public Date postponed_at;

    public String nombreInstitucion(){
        return institution.name;
    }

    public String direccionInstitucion(){
        return institution.address;
    }

    public Task obtenerTarea(EnumTareas tipo) throws IllegalArgumentException  {
        Task ret = null;

        for (Task tarea : tasks) {
            if (tarea.type == tipo.id){
                ret = tarea;
                break;
            }
        }

        if(ret == null){
            throw new IllegalArgumentException();
        }

        return ret;
    }
}
