package Modelo;

public class Risk {

    public int id;

    public String code;

    public String description;

    @Override
    public String toString() {
        return code + " - " +description;
    }
}
