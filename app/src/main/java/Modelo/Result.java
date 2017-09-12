package Modelo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable
public abstract class Result {

    @DatabaseField(generatedId = true)
    public int id;

    @DatabaseField
    public int type;

    @DatabaseField
    public Date completed_at;

}
