package Modelo;

import java.io.Serializable;

public class Risk implements Serializable{

    public int id;

    public String code;

    public String description;

    @Override
    public String toString() {
        return code + " - " +description;
    }
}
