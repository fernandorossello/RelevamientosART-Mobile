package Modelo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

public class Result implements Serializable {

    @DatabaseField(generatedId = true)
    public int id;

    @DatabaseField
    public int type;

    @DatabaseField
    public Date completed_at;

    @DatabaseField(foreign = true)
    public Task task;

}
