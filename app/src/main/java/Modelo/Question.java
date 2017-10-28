package Modelo;


import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable
public class Question implements Serializable{

    @DatabaseField(foreign = true)
    public RGRLResult result;

    @DatabaseField(generatedId = true)
    public int idDB;

    @Expose
    @DatabaseField
    public int id;

    @DatabaseField
    public String category;

    @DatabaseField
    public String description;

    @Expose
    @DatabaseField
    public int answer;

    @Override
    public boolean equals(Object obj) {
        return this.id == ((Question)obj).id;
    }
}
