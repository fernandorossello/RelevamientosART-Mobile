package Modelo.Enums;
import java.io.Serializable;

public enum EnumTareas implements Serializable{

    CAPACITACION(0, "Brindar capacitación","Capacitación"),
    RGRL(1, "Confección planilla RGRL","RGRL"),
    RAR(2,"Confección planilla de Relevamiento de agentes de riesgo","RAR");

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
