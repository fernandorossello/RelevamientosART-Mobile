package Modelo;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

@DatabaseTable
public class VisitRecord implements Serializable{

    @DatabaseField(generatedId = true)
    public int id;

    @Expose
    @DatabaseField
    public Date completed_at;

    @Expose
    @DatabaseField
    public String observations;

}
