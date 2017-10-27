package Modelo;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import Modelo.Enums.EnumAnswer;

@DatabaseTable
public class Question implements Serializable{

    @DatabaseField(foreign = true)
    public RGRLResult result;

    @DatabaseField(generatedId = true)
    public int idDB;

    @DatabaseField
    public int id;

    @DatabaseField
    public String description;

    @DatabaseField
    public int answer;

    @Override
    public boolean equals(Object obj) {
        return this.id == ((Question)obj).id;
    }
}
