package Modelo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable
public class VisitRecord {

    @DatabaseField(id = true)
    public int id;

    @DatabaseField
    public Date completed_at;

    @DatabaseField
    public String observations;

}
