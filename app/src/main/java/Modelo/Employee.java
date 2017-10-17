package Modelo;

import com.j256.ormlite.field.DatabaseField;
import java.io.Serializable;

public class Employee implements Serializable{

    @DatabaseField(generatedId = true)
    public int id;

    @DatabaseField
    public String cuil;

    @DatabaseField
    public String name;

    @DatabaseField
    public String lastName;

    @DatabaseField
    public String type;

    public String nombreCompleto(){
        if(name != null && lastName != null)
                return name + " " + lastName;
        else return "";
    }
}
