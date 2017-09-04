package Modelo;

public enum EnumTareas {

    PENDIENTE(1,"Confección planilla de Relevamiento de agentes de riesgo"),
    ASIGNADA(2, "Confección planilla RGRL"),
    ENPROCESO(3, "Brindar capacitación");


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
