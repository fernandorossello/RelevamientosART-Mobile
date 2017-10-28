package Modelo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

import Modelo.Enums.EnumStatus;


@DatabaseTable
public abstract class Result implements Serializable {

    @DatabaseField(generatedId = true)
    public int id;

    @DatabaseField
    public int type;

    @DatabaseField
    public Date completed_at;

    @DatabaseField(foreign = true)
    public Task task;

    public abstract EnumStatus getStatus();
}
