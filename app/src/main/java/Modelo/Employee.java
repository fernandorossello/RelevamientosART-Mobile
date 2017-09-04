package Modelo;

public class Employee {

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
