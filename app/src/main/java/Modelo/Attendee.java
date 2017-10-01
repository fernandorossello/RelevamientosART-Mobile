package Modelo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable
public class Attendee extends Employee implements Serializable {

    @DatabaseField(foreign = true)
    public CAPResult result;
}
