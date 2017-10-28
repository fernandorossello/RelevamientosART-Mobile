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

    @Expose
    @DatabaseField
    public String category;

    @Expose
    @DatabaseField
    public String description;

    @Expose
    @DatabaseField
    public String answer;

    @DatabaseField
    public int answer_code;

    @Override
    public boolean equals(Object obj) {
        return this.id == ((Question)obj).id;
    }
}
