package Modelo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Zone {

    @DatabaseField(id = true)
    public int id;

    @DatabaseField
    public String name;

}
