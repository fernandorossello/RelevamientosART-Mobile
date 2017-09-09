package Modelo.Enums;

public enum EnumTareas {

    RAR(1,"Confección planilla de Relevamiento de agentes de riesgo"),
    RGRL(2, "Confección planilla RGRL"),
    CAPACITACION(3, "Brindar capacitación");


    public String name;

    public int id;

    EnumTareas(int id, String name) {
        this.name = name;
        this.id = id;
    }

    public static EnumTareas getById(int id) {
        for(EnumTareas e : values()) {
            if(e.id == id) return e;
        }
        return null;
    }
}
