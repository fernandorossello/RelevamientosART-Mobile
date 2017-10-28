package Modelo;


import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Risk implements Serializable{

    @DatabaseField(generatedId = true)
    public int id;

    @Expose
    @DatabaseField
    public String code;

    @Expose
    @DatabaseField
    public String description;

    @Override
    public String toString() {
        return code + " - " +description;
    }

    @DatabaseField(foreign = true)
    public WorkingMan working_man;

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Risk))return false;
        Risk otherRisk = (Risk)other;
        return code.equals(otherRisk.code);
    }
}
