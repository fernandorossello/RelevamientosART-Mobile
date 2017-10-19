package Modelo;


import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

import Modelo.Enums.EnumAnswer;

public class Question implements Serializable{
    @DatabaseField(foreign = true)
    public RGRLResult result;

    @DatabaseField
    public int id;

    @DatabaseField
    public String description;

    @DatabaseField
    public EnumAnswer answer;
}
