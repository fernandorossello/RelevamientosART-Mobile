package Modelo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Institution {

    @DatabaseField(id = true)
    public int id;

    @DatabaseField
    public String name;

    @DatabaseField
    public String cuit;

    @DatabaseField
    public String contract;

    @DatabaseField
    public String address;

    @DatabaseField
    public String city;

    @DatabaseField
    public String province;

    @DatabaseField
    public String postal_code;

    @DatabaseField
    public int number;

    @DatabaseField
    public String activity;

    @DatabaseField
    public int surface;

    @DatabaseField
    public int workers_count;

    @DatabaseField
    public int institutions_count;

    @DatabaseField
    public float latitude;

    @DatabaseField
    public float longitud;

    @DatabaseField
    public String phone;

    @DatabaseField(foreign = true)
    public Zone zone;

}
