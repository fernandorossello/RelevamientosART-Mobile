package Modelo;

import java.io.Serializable;

public class Employee implements Serializable{

    public int id;

    public String cuil;

    public String name;

    public String lastName;

    public String sector;

    public String type;

    public String nombreCompleto(){
        if(name != null && lastName != null)
                return name + " " + lastName;
        else return "";
    }
}
