package Modelo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable
public class Image implements Serializable{

    @DatabaseField(generatedId = true)
    public int id;

    @DatabaseField
    public String URLImage;

    @DatabaseField(foreign = true)
    public Visit visit;

}
