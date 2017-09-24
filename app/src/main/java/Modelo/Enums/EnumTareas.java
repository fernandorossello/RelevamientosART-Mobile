package Modelo.Enums;

import java.io.Serializable;

public enum EnumTareas implements Serializable{

    RAR(1,"Confección planilla de Relevamiento de agentes de riesgo","RAR"),
    RGRL(2, "Confección planilla RGRL","RGRL"),
    CAPACITACION(3, "Brindar capacitación","Capacitacion");


    public String name;
    public String shortName;
    public int id;

    EnumTareas(int id, String name,String shortName) {
        this.name = name;
        this.id = id;
        this.shortName = shortName;
    }

    public static EnumTareas getById(int id) {
        for(EnumTareas e : values()) {
            if(e.id == id) return e;
        }
        return null;
    }
}
