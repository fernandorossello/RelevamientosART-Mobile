
package Modelo.Enums;

import java.io.Serializable;

public enum EnumStatus  implements Serializable{

    PENDIENTE(0,"Pendiente"),
    ASIGNADA(1, "Asignada"),
    ENPROCESO(2, "En proceso"),
    FINALIZADA(3,"Finalizada"),
    ENVIADA(4,"Enviada");


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