package Modelo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Employee {

    @DatabaseField
    public String cuil;

    @DatabaseField
    public String name;

    @DatabaseField
    public String lastName;

    @DatabaseField
    public String sector;

    @DatabaseField
    public String type;

    public String nombreCompleto(){
        if(name != null && lastName != null)
                return name + " " + lastName;
        else return "";
    }
}
