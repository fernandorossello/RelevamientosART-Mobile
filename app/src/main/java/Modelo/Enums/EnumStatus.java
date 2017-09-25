package Modelo.Enums;

import java.io.Serializable;

public enum EnumStatus  implements Serializable{

    PENDIENTE(1,"Pendiente"),
    ASIGNADA(2, "Asignada"),
    ENPROCESO(3, "En proceso"),
    POSTERGADA(4, "Postergada"),
    FINALIZADA(5,"Finalizada"),
    ENVIADA(6,"Enviada"),
    CANCELADA(7,"Cancelada");


    public java.lang.String name;

    public int id;

    EnumStatus(int id, java.lang.String name) {
        this.name = name;
        this.id = id;
    }

    public static EnumStatus getById(int id) {
        for(EnumStatus e : values()) {
            if(e.id == id) return e;
        }
        return null;
    }
}
