package Modelo;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.io.Serializable;

@DatabaseTable
public class Noise implements Serializable {

    @DatabaseField(generatedId = true)
    public int id;

    @Expose
    @DatabaseField
    public Double decibels;

    @Expose
    @DatabaseField
    public String description;

    @DatabaseField(foreign = true)
    public Visit visit;

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Noise))return false;
        Noise otherRisk = (Noise)other;
        return id == otherRisk.id;
    }
}
