package Modelo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable
public class Image implements Serializable{

    @DatabaseField(generatedId = true)
    public int id;

    @DatabaseField
    public String url_image;

    @DatabaseField(foreign = true)
    public Visit visit;

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Image))return false;
        Image otherImage = (Image)other;

        return id == otherImage .id;
    }

}
