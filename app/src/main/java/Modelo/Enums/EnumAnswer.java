package Modelo.Enums;

/**
 * Created by Tomás on 8/10/2017.
 */

public enum EnumAnswer {
    SI(1,"Sí"),
    NO(2, "No"),
    NOAPLICA(3, "No Aplica");

    public String name;
    public int id;

    EnumAnswer(int id, String name) {
        this.name = name;
        this.id = id;
    }
}
