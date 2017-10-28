package Modelo;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import java.io.Serializable;

public class Employee implements Serializable{

    @DatabaseField(generatedId = true)
    public int id;

    @Expose
    @DatabaseField
    public String cuil;

    @Expose
    @DatabaseField
    public String name;

    @Expose
    @DatabaseField
    public String last_name;

    @Expose
    @DatabaseField
    public String sector;

    @DatabaseField
    public String type;

    public String nombreCompleto(){
        if(name != null && last_name != null)
                return name + " " + last_name;
        else return "";
    }
}
