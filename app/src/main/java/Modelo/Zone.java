package Modelo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable
public class Zone implements Serializable {

    @DatabaseField(id = true)
    public int id;

    @DatabaseField
    public String name;

}
