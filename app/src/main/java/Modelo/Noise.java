package Modelo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.io.Serializable;

@DatabaseTable
public class Noise implements Serializable {

    @DatabaseField(generatedId = true)
    public int id;

    @DatabaseField
    public Double decibels;

    @DatabaseField
    public String description;

    @DatabaseField(foreign = true)
    public Visit visit;
}
