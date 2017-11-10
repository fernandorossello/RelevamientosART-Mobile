package Modelo;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import Modelo.Enums.EnumTareas;

@DatabaseTable
public class Visit implements Serializable {

    public Visit(){
        tasks = new ArrayList<>();
        images=  new ArrayList<>();
        noises = new ArrayList<>();

        completed_at = new Date();
    }

    @Expose
    @DatabaseField(id = true)
    public int id;

    @DatabaseField(foreign = true,foreignAutoRefresh = true)
    public Institution institution;

    @DatabaseField
    public int type;

    @DatabaseField
    public int priority;

    @ForeignCollectionField
    public Collection<Task> tasks;

    @Expose
    @ForeignCollectionField
    public Collection<Image> images;

    @Expose
    @DatabaseField(foreign = true,foreignAutoRefresh = true)
    public VisitRecord visit_record;

    @DatabaseField
    public int status;

    @DatabaseField
    public Date to_visit_on;

    @DatabaseField
    @Expose
    public Date completed_at;

    @DatabaseField
    public int user_id;

    @Expose
    @ForeignCollectionField
    public Collection<Noise> noises;

    public Task obtenerTarea(EnumTareas tipo) throws IllegalArgumentException  {
        Task ret = null;

        for (Task tarea : tasks) {
            if (tarea.task_type == tipo.id){
                ret = tarea;
                break;
            }
        }

        if(ret == null){
            throw new IllegalArgumentException();
        }

        return ret;
    }

    public Boolean tieneTarea(EnumTareas tipo){

        for (Task tarea : tasks) {
            if (tarea.task_type == tipo.id){
                return true;
            }
        }
        return false;
    }

    public String toJson() {
        return new GsonBuilder()
                    .setPrettyPrinting()
                    .disableHtmlEscaping()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create()
                    .toJson(this);
    }
}
