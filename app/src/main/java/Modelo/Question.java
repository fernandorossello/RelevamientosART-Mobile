package Modelo;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import Modelo.Enums.EnumAnswer;

@DatabaseTable
public class Question implements Serializable{

    @DatabaseField(foreign = true)
    public RGRLResult result;

    @DatabaseField(id = true)
    public int id;

    @DatabaseField
    public String description;

    @DatabaseField
    public int answer;
}
